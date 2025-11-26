import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CarritoService } from '../../services/carrito.service'; // Ajusta la ruta
import { Header } from '../../components/header/header.component'; // Ajusta si tienes header
import { Footer } from '../../components/footer/footer.component'; // Ajusta si tienes footer

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, Header, Footer], // Importa Header/Footer si los usas
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
})
export class CartComponent implements OnInit {

  carrito: any = null;
  cargando = true;

  constructor(
    private carritoService: CarritoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCarrito();
  }

  cargarCarrito() {
    this.cargando = true;
    this.carritoService.verCarrito().subscribe({
      next: (data) => {
        this.carrito = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando carrito', err);
        this.cargando = false;
      }
    });
  }

  actualizarCantidad(idProducto: number, cantidadActual: number, cambio: number) {
    const nuevaCantidad = cantidadActual + cambio;
    if (nuevaCantidad < 1) return; // No permitir 0 o negativos

    this.carritoService.actualizarCantidad(idProducto, nuevaCantidad).subscribe({
      next: (data) => {
        this.carrito = data; // Actualizamos la vista con la respuesta del backend
      },
      error: (err) => alert('No se pudo actualizar (quizás no hay stock)')
    });
  }

  eliminarItem(idProducto: number) {
    if(!confirm('¿Eliminar producto?')) return;
    
    this.carritoService.eliminarProducto(idProducto).subscribe({
      next: (data) => this.carrito = data
    });
  }

  vaciarCarrito() {
    if(!confirm('¿Vaciar todo el carrito?')) return;

    this.carritoService.vaciarCarrito().subscribe({
      next: () => {
        this.cargarCarrito(); // Recargar (debería venir vacío)
      }
    });
  }

  irAlCheckout() {
    this.router.navigate(['/checkout']);
  }
}