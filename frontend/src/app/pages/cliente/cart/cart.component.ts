import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { CarritoService } from '../../../services/carrito.service';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CarritoComponent implements OnInit {
  private carritoService = inject(CarritoService);
  private router = inject(Router);

  carrito: any = null;
  cargando = true;
  procesandoId: number | null = null;

  ngOnInit() {
    this.cargarCarrito();
  }

  get totalCalculado(): number {
    if (!this.carrito || !this.carrito.detalles) return 0;

    // Suma: (Precio * Cantidad) de cada item
    return this.carrito.detalles.reduce((acc: number, item: any) => {
      // Usamos item.precioUnitario (que ya vimos que sí llega)
      const subtotalItem = item.cantidad * item.precioUnitario;
      return acc + subtotalItem;
    }, 0);
  }

  cargarCarrito() {
    this.cargando = true;
    this.carritoService.verCarrito().subscribe({
      next: (data) => {
        // 🔍 MIRA LA CONSOLA DEL NAVEGADOR (F12)
        console.log('📦 RESPUESTA DEL BACKEND:', data);
        console.log('🔍 PRIMER ITEM:', data.detalles[0]);

        this.carrito = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando carrito', err);
        this.cargando = false;
      },
    });
  }

  cambiarCantidad(item: any, cambio: number) {
    // 1. VALIDACIÓN DE ID (Aquí está fallando tu botón)
    if (!item.idProducto) {
      alert('ERROR CRÍTICO: El backend mandó idProducto = NULL. Revisa tu Mapper en Java.');
      console.error('Item sin ID:', item);
      return;
    }

    const nuevaCantidad = item.cantidad + cambio;
    if (nuevaCantidad < 1) return;

    // Evitar doble clic
    if (this.procesandoId === item.idProducto) return;

    this.procesandoId = item.idProducto;

    this.carritoService.actualizarCantidad(item.idProducto, nuevaCantidad).subscribe({
      next: (res) => {
        // Actualizamos la vista completa con lo que devuelve el backend
        this.carrito = res;
        this.procesandoId = null;
      },
      error: (err) => {
        console.error(err);
        this.procesandoId = null;
      },
    });
  }

  eliminarItem(idProducto: number) {
    if (!idProducto) {
      alert('No se puede eliminar: ID del producto es NULL');
      return;
    }

    if (!confirm('¿Quitar producto del carrito?')) return;

    this.cargando = true;
    this.carritoService.eliminarProducto(idProducto).subscribe({
      next: (res) => {
        this.carrito = res;
        this.cargando = false;
      },
      error: () => (this.cargando = false),
    });
  }

  vaciar() {
    if (!confirm('¿Vaciar todo el carrito?')) return;

    this.cargando = true;
    this.carritoService.vaciarCarrito().subscribe({
      next: () => {
        this.carrito = null;
        this.cargando = false;
      },
      error: () => (this.cargando = false),
    });
  }

  irAPagar() {
    // 1. Validación de seguridad (opcional, por si habilitaron el botón con hack)
    if (!this.carrito || !this.carrito.detalles || this.carrito.detalles.length === 0) {
      alert('Tu carrito está vacío, agrega productos primero.');
      return;
    }

    // 2. Navegación
    console.log('Navegando al checkout...');
    this.router.navigate(['/checkout']);
  }
}
