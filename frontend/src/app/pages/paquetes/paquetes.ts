import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Header } from '../../header/header';
import { Footer } from '../../footer/footer';
import { CarritoService } from '../../services/carrito';
import { Paquete } from '../../interfaces/paquete';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { ProductoService } from '../../services/producto';
import { ProductoResponse } from '../../interfaces/producto-response';

@Component({
  selector: 'app-paquetes',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    Header,
    Footer,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './paquetes.html',
  styleUrls: ['./paquetes.css'],
})
export class Paquetes implements OnInit {

  // filtros
  precioMin: number = 0;
  precioMax: number = 150;
  resenaSeleccionada: number = 0;
  busquedaAvanzadaActiva: boolean = false;

  // paginación
  paginaActual = 1;
  elementosPorPagina = 6;

  // estado UI
  paquetes: Paquete[] = [];
  cargando = true;

  // carrito / modal
  carrito: any[] = [];
  modalCarritoAbierto = false;

  constructor(
    private carritoService: CarritoService,
    private ProductoService: ProductoService
  ) {}

  ngOnInit(): void {

    /** Cargar productos desde backend */
    this.ProductoService.listarActivos().subscribe({
      next: (data: ProductoResponse[]) => {

        this.paquetes = data.map(p => ({
          id: p.id,
          nombre: p.nombre,
          precio: Number(p.precio),
          stars: 4,
          img: p.imagenUrl || '/img/placeholder.png',
          cantidad: 1
        }));

        this.cargando = false;
      },
      error: err => {
        console.error('Error cargando productos:', err);
        this.cargando = false;
      }
    });

    /** Mantener carrito sincronizado con el BehaviorSubject */
    this.carritoService.carrito$.subscribe(c => {
      this.carrito = c;
    });
  }

  /* PAGINACIÓN */
  get paginas() {
    return Array.from(
      { length: Math.max(1, Math.ceil(this.paquetesFiltrados.length / this.elementosPorPagina)) },
      (_, i) => i + 1
    );
  }

  get paquetesFiltrados() {
    let filtrados = this.paquetes.filter(p =>
      p.precio >= this.precioMin &&
      p.precio <= this.precioMax &&
      (this.resenaSeleccionada === 0 || p.stars === this.resenaSeleccionada)
    );

    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return filtrados.slice(inicio, inicio + this.elementosPorPagina);
  }

  /* Filtros */
  setResena(n: number) {
    this.resenaSeleccionada = n;
    this.paginaActual = 1;
  }

  toggleBusquedaAvanzada() {
    this.busquedaAvanzadaActiva = !this.busquedaAvanzadaActiva;
  }

  setPagina(n: number) {
    this.paginaActual = n;
  }

  nextPage() {
    if (this.paginaActual < this.paginas.length) {
      this.paginaActual++;
    }
  }

  /* ------- CARRITO ------- */

  abrirCarrito() {
    this.modalCarritoAbierto = true;
  }

  cerrarModal() {
    this.modalCarritoAbierto = false;
  }

  agregarAlCarrito(p: Paquete) {
    this.carritoService.agregar(p);
  }

  sumarCantidad(i: number) {
  this.carritoService.sumar(this.carrito[i].id);
}

restarCantidad(i: number) {
  this.carritoService.restar(this.carrito[i].id);
}

eliminar(i: number) {
  this.carritoService.eliminar(this.carrito[i].id);
}

get totalCarrito() {
  return this.carritoService.total();
}
}