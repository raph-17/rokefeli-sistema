import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Header } from '../../components/header/header.component'; // Ajusta ruta
import { Footer } from '../../components/footer/footer.component'; // Ajusta ruta
import { CarritoService } from '../../services/carrito.service';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { ProductoService } from '../../services/producto.service'; // Ajusta nombre archivo
import { ProductoResponse } from '../../interfaces/producto-response';

// Interfaz local para la vista (puedes moverla a un archivo aparte)
interface PaqueteUI {
  id: number;
  nombre: string;
  precio: number;
  stars: number;
  img: string;
  descripcion?: string;
}

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
  templateUrl: './paquetes.component.html', // Sugiero usar .component.html
  styleUrls: ['./paquetes.component.css'], // Sugiero usar .component.css
})
export class Paquetes implements OnInit {

  // --- FILTROS ---
  precioMin: number = 0;
  precioMax: number = 200; // Ajustado un poco más alto
  resenaSeleccionada: number = 0;
  busquedaAvanzadaActiva: boolean = false;

  // --- PAGINACIÓN ---
  paginaActual = 1;
  elementosPorPagina = 6;

  // --- ESTADO UI ---
  paquetes: PaqueteUI[] = []; // Lista completa
  cargando = true;
  agregandoId: number | null = null; // Para mostrar spinner en el botón específico

  constructor(
    private carritoService: CarritoService,
    private productoService: ProductoService // Nombre corregido (camelCase)
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos() {
    this.cargando = true;
    this.productoService.listarActivos().subscribe({
      next: (data: any[]) => { // data: ProductoResponse[]
        // Mapeamos la respuesta del Backend a la estructura visual
        this.paquetes = data.map(p => ({
          id: p.id,
          nombre: p.nombre,
          precio: Number(p.precio),
          stars: 4, // Hardcodeado o calculado si tuvieras reseñas
          img: p.imagenUrl || '/assets/img/placeholder.png', // Ruta corregida
          descripcion: p.descripcion
        }));
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando productos:', err);
        this.cargando = false;
      }
    });
  }

  /* ===========================
     LÓGICA DEL CARRITO (API)
     =========================== */

  agregarAlCarrito(producto: PaqueteUI) {
    // Evita doble click
    if (this.agregandoId === producto.id) return;

    this.agregandoId = producto.id;

    // Llamada al servicio REAL conectado a Spring Boot
    // Cantidad fija en 1 para el catálogo
    this.carritoService.agregarProducto(producto.id, 1).subscribe({
      next: () => {
        alert(`¡${producto.nombre} agregado al carrito! 🐝`);
        this.agregandoId = null;
        // El contador del header se actualiza solo gracias al BehaviorSubject del servicio
      },
      error: (err) => {
        console.error(err);
        alert('No se pudo agregar (verifique stock o inicie sesión)');
        this.agregandoId = null;
      }
    });
  }

  /* ===========================
     FILTROS Y PAGINACIÓN
     =========================== */

  get paquetesFiltrados() {
    // Filtro en memoria (Frontend)
    let filtrados = this.paquetes.filter(p =>
      p.precio >= this.precioMin &&
      p.precio <= this.precioMax &&
      (this.resenaSeleccionada === 0 || p.stars === this.resenaSeleccionada)
    );

    // Paginación en memoria
    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return filtrados.slice(inicio, inicio + this.elementosPorPagina);
  }

  get paginas() {
    // Calculamos el total de páginas basado en los items filtrados
    const totalItems = this.paquetes.filter(p =>
      p.precio >= this.precioMin &&
      p.precio <= this.precioMax &&
      (this.resenaSeleccionada === 0 || p.stars === this.resenaSeleccionada)
    ).length;

    return Array.from(
      { length: Math.max(1, Math.ceil(totalItems / this.elementosPorPagina)) },
      (_, i) => i + 1
    );
  }

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
  
  prevPage() {
    if (this.paginaActual > 1) {
      this.paginaActual--;
    }
  }
}