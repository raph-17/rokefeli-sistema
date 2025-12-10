import { Component, OnInit, inject, NgZone } from '@angular/core'; // NgZone es vital para callbacks externos
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CheckoutService } from '../../../services/checkout.service';
import { CarritoService } from '../../../services/carrito.service';
import { AuthService } from '../../../services/auth.service';

declare var Culqi: any; // Declaramos la variable global de Culqi

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
})
export class CheckoutComponent implements OnInit {
  private checkoutService = inject(CheckoutService);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private ngZone = inject(NgZone); // Para volver al contexto de Angular desde JS puro

  // Datos
  venta: any = null;
  deptos: any[] = [];
  provs: any[] = [];
  dists: any[] = [];
  tarifas: any[] = [];

  // Selección
  tarifaSeleccionada: any = null;
  cargando = false;

  // Formularios
  ubicacionForm: FormGroup = this.fb.group({
    idDepartamento: [null, Validators.required],
    idProvincia: [null, Validators.required],
    idDistrito: [null, Validators.required],
    direccion: ['', Validators.required],
    referencia: [''],
  });

  // Configuración Culqi
  CULQI_PUBLIC_KEY = 'pk_test_K9fiqBbCD5SuopIq';

  ngOnInit() {
    this.iniciarProcesoCompra();
    this.configurarCulqi();
  }

  iniciarProcesoCompra() {
    this.cargando = true;
    // 1. Convertimos carrito a venta PENDIENTE
    this.checkoutService.crearVentaPreliminar().subscribe({
      next: (venta) => {
        this.venta = venta;
        this.cargarDepartamentos();
        this.cargando = false;
      },
      error: () => {
        alert('Error al iniciar checkout. Tu carrito podría estar vacío.');
        this.router.navigate(['/carrito']);
      },
    });
  }

  // --- CASCADA DE UBICACIONES ---
  cargarDepartamentos() {
    this.checkoutService.getDepartamentos().subscribe((data) => (this.deptos = data));
  }

  onDeptoChange() {
    const id = this.ubicacionForm.get('idDepartamento')?.value;
    this.provs = [];
    this.dists = [];
    this.tarifas = [];
    this.checkoutService.getProvincias(id).subscribe((data) => (this.provs = data));
  }

  onProvChange() {
    const id = this.ubicacionForm.get('idProvincia')?.value;
    this.dists = [];
    this.tarifas = [];
    this.checkoutService.getDistritos(id).subscribe((data) => (this.dists = data));
  }

  onDistChange() {
    const id = this.ubicacionForm.get('idDistrito')?.value;
    this.tarifas = [];
    this.tarifaSeleccionada = null;

    // Buscar tarifas para ese distrito (Agencias disponibles)
    this.checkoutService.getTarifasPorDistrito(id).subscribe((data) => {
      this.tarifas = data;
    });
  }

  seleccionarTarifa(tarifa: any) {
    this.tarifaSeleccionada = tarifa;
  }

  // --- CÁLCULOS ---
  get montoSubtotal(): number {
    return this.venta ? this.venta.montoTotal : 0;
  }
  get montoEnvio(): number {
    return this.tarifaSeleccionada ? this.tarifaSeleccionada.costoEnvio : 0;
  }
  get montoTotal(): number {
    return this.montoSubtotal + this.montoEnvio;
  }

  // --- INTEGRACIÓN CULQI ---
  configurarCulqi() {
    Culqi.publicKey = this.CULQI_PUBLIC_KEY;

    // Escuchar respuesta de Culqi (Callback global)
    (window as any).culqi = () => {
      if (Culqi.token) {
        Culqi.close();
        // ¡Tenemos Token! Volvemos a zona Angular para procesar
        this.ngZone.run(() => {
          this.cargando = true; // Aseguramos que se vea tu spinner de Angular
          this.enviarPagoAlBackend(Culqi.token.id);
        });
      } else {
        console.error(Culqi.error);
        alert(Culqi.error.user_message);
        this.cargando = false;
      }
    };
  }

  pagar() {
    if (this.ubicacionForm.invalid || !this.tarifaSeleccionada) {
      alert('Por favor completa la ubicación y selecciona una agencia de envío.');
      return;
    }

    this.cargando = true;

    // Configurar Culqi con el monto actualizado (en céntimos)
    Culqi.settings({
      title: 'Colmenares Rokefeli',
      currency: 'PEN',
      description: `Pedido #${this.venta.id}`,
      amount: Math.round(this.montoTotal * 100), // S/ 100.00 -> 10000
    });

    // Abrir pasarela (Popup de Culqi)
    Culqi.open();
  }

  enviarPagoAlBackend(tokenCulqi: string) {
    const metodoDetectado = this.detectarMetodoPago(Culqi.token);

    console.log('Método detectado por Culqi:', metodoDetectado);

    const payload = {
      idVenta: this.venta.id,
      idTarifaEnvio: this.tarifaSeleccionada.id,
      monto: this.montoTotal,
      emailCliente: this.authService.obtenerEmailDelToken(),
      tokenCulqi: tokenCulqi,
      direccionEnvio: this.ubicacionForm.get('direccion')?.value, // Antes era direccionEntrega
      referenciaCliente: this.ubicacionForm.get('referencia')?.value, // Agregamos la referencia
      metodoPago: metodoDetectado,
    };

    this.checkoutService.procesarPago(payload).subscribe({
      next: (res) => {
        if (res.estadoPago === 'APROBADO') {
          // Éxito total
          this.carritoService.actualizarContador(); // Reset carrito icono
          alert('¡Compra Exitosa! 🐝 Gracias por tu preferencia.');
          this.router.navigate(['/catalogo']);
        } else {
          alert('El pago fue denegado por el banco.');
        }
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        alert('Error procesando el pago en el servidor.');
        this.cargando = false;
      },
    });
  }

  // Helper para determinar si es Tarjeta, Yape, etc.
  detectarMetodoPago(token: any): string {
    // 1. Si tiene 'iin', es definitivamente una TARJETA (Visa, Master, etc.)
    if (token.iin) {
      return 'TARJETA';
    }

    // 2. Si el objeto tiene un tipo específico (depende de la versión de Culqi)
    // A veces Culqi manda token.type = 'yape'
    if (token.type === 'yape' || token.object === 'yape') {
      return 'YAPE';
    }

    // 3. Por defecto (Fallback)
    // Si no detectamos tarjeta, asumimos que fue el otro método activo
    return 'TARJETA';
  }
}
