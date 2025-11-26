import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../../services/carrito';
import { VentaService } from '../../services/venta-service';
import { PagoService } from '../../services/pago-service';
import { Header } from '../../header/header';
import { Footer } from '../../footer/footer';
import { MatAnchor } from "@angular/material/button";
import { MatFormField } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmar-pago',
  standalone: true,
  imports: [
    Header, Footer, CommonModule, ReactiveFormsModule,
    MatAnchor, MatFormField, MatInputModule, MatRadioModule, MatCardModule
  ],
  templateUrl: './confirmar-pago.html',
  styleUrls: ['./confirmar-pago.css'],
})
export class ConfirmarPagoComponent {

  formPago!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private carritoService: CarritoService,
    private ventaService: VentaService,
    private pagoService: PagoService,
    private router: Router
  ) {}

  ngOnInit() {
    this.formPago = this.fb.group({
      metodo: ['tarjeta', Validators.required],
      numeroTarjeta: ['', [Validators.required, Validators.pattern(/^[0-9]{16}$/)]],
      titular: ['', Validators.required],
      expiracion: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^[0-9]{3}$/)]],
    });
  }

  get carrito() {
    return this.carritoService.getCarrito();
  }

  get total() {
    return this.carritoService.total();
  }

  confirmarPedido() {
    if (this.formPago.invalid) {
      this.formPago.markAllAsTouched();
      return;
    }

    /** 1. Armamos la venta */
    const ventaPayload = {
      idUsuario: 1, // ⚠ Aquí debes colocar el ID real del usuario logueado
      detalles: this.carrito.map(item => ({
        idProducto: item.id,
        cantidad: item.cantidad
      }))
    };

    this.ventaService.registrarVentaOnline(ventaPayload).subscribe({
      next: (venta) => {

        const idVenta = venta.id;

        /** 2. Armamos el pago */
        const pagoPayload = {
          idVenta: idVenta,
          idTarifaEnvio: 1, // si existe este dato en tu sistema
          metodoPago: "TARJETA",
          monto: this.total,
          tokenCulqi: "FAKE-TOKEN", // aquí tienes que integrar culqi después
          emailCliente: "cliente@correo.com"
        };

        /** 3. Procesar pago */
        this.pagoService.pagar(pagoPayload).subscribe({
          next: () => {

            /** 4. Vaciar carrito y navegar */
            this.carritoService.vaciarCarrito();
            this.router.navigate(['/compra-exitosa']);
          },
          error: err => {
            console.error("Error en pago:", err);
            alert("Error al procesar pago");
          }
        });
      },
      error: err => {
        console.error("Error creando venta:", err);
        alert("No se pudo registrar la venta.");
      }
    });
  }
}