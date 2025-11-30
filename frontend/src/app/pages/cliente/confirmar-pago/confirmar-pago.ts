import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

// Servicios
import { CarritoService } from '../../../services/carrito.service';
import { VentaService } from '../../../services/venta.service';
import { PagoService } from '../../../services/pago.service'; // <--- Asegúrate de tener este servicio creado (ver abajo si no)

// Material
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-confirmar-pago',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule,
    MatButtonModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatRadioModule, 
    MatCardModule
  ],
  templateUrl: './confirmar-pago.html',
  styleUrls: ['./confirmar-pago.css'],
})
export class ConfirmarPagoComponent implements OnInit {

  formPago!: FormGroup;
  
  // Variable local para guardar lo que trae el backend
  carritoData: any = null; 
  cargando = true;

  constructor(
    private fb: FormBuilder,
    private carritoService: CarritoService,
    private ventaService: VentaService,
    private pagoService: PagoService,
    private router: Router
  ) {}

  ngOnInit() {
    // 1. Inicializar Formulario
    this.formPago = this.fb.group({
      metodo: ['tarjeta', Validators.required],
      numeroTarjeta: ['', [Validators.required, Validators.pattern(/^[0-9]{16}$/)]],
      titular: ['', Validators.required],
      expiracion: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^[0-9]{3}$/)]]
    });

    // 2. Cargar datos del carrito desde el Backend
    this.cargarDatosCarrito();
  }

  cargarDatosCarrito() {
    this.cargando = true;
    this.carritoService.verCarrito().subscribe({
      next: (data) => {
        this.carritoData = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar carrito', err);
        this.cargando = false;
      }
    });
  }

  // Getter para el total (ahora lo lee de la variable local, no del servicio)
  get totalPagar(): number {
    return this.carritoData ? this.carritoData.montoTotal : 0;
  }

  confirmarPedido() {
    if (this.formPago.invalid) {
      this.formPago.markAllAsTouched();
      return;
    }

    if (!this.carritoData || !this.carritoData.detalles) {
      alert('El carrito está vacío o no se ha cargado.');
      return;
    }

    /** 1. Armamos el payload de la VENTA */
    const ventaPayload = {
      // El ID de usuario lo saca el Backend del token, mandamos 0 o null
      idUsuario: 0, 
      detalles: this.carritoData.detalles.map((item: any) => ({
        // OJO: En el response del backend es item.producto.id
        idProducto: item.producto.id, 
        cantidad: item.cantidad
      }))
    };

    this.ventaService.registrarVentaOnline(ventaPayload).subscribe({
      next: (venta: any) => {
        console.log('Venta registrada:', venta);
        
        const idVenta = venta.id;

        /** 2. Armamos el payload del PAGO */
        const pagoPayload = {
          idVenta: idVenta,
          idTarifaEnvio: 1, // TODO: Debes obtener esto de un select de tarifas en el HTML
          monto: this.totalPagar + 10, // Sumar costo de envío (Hardcodeado 10 por ahora)
          metodoPago: "TARJETA",
          emailCliente: "cliente@ejemplo.com", // TODO: Sacar del token o formulario
          tokenCulqi: "tkn_test_simulado_123" // TODO: Aquí integrarías Culqi.js
        };

        // 3. Llamar al servicio de pago (Simulado por ahora con console.log si no tienes el servicio)
        console.log('Enviando pago...', pagoPayload);
        
        
        this.pagoService.pagar(pagoPayload).subscribe({
          next: () => {
            this.finalizarCompra();
          },
          error: (err) => alert("Error en el pago: " + err.message)
        });
        
        // POR AHORA (Para que avance):
        this.finalizarCompra();
      },
      error: err => {
        console.error("Error creando venta:", err);
        alert("No se pudo registrar la venta. Intente nuevamente.");
      }
    });
  }

  finalizarCompra() {
    // Vaciar carrito en el backend (ya no devuelve nada útil, solo void)
    this.carritoService.vaciarCarrito().subscribe(() => {
      alert('¡Compra Exitosa! 🐝');
      this.router.navigate(['/paquetes']); // O a una pagina de 'gracias'
    });
  }
}