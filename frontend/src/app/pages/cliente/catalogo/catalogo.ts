import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../../../services/carrito.service';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { ProductoService } from '../../../services/producto.service';
import { CategoriaService } from '../../../services/categoria.service';

interface CatalogoUI {
  id: number;
  nombre: string;
  precio: number;
  img: string;
  descripcion?: string;
}

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [FormsModule, CommonModule, MatButtonModule, RouterModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css'],
})
export class Catalogo implements OnInit {
  // --- FILTROS (Solo backend) ---
  filtroNombre: string = '';
  filtroCategoria: number | null = null;
  categorias: any[] = [];

  // --- PAGINACIÓN ---
  paginaActual = 1;
  elementosPorPagina = 6;

  // --- ESTADO UI ---
  paquetes: CatalogoUI[] = [];
  cargando = true;
  agregandoId: number | null = null;

  constructor(
    private carritoService: CarritoService,
    private productoService: ProductoService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarProductos();
  }

  cargarCategorias() {
    this.categoriaService.findAllClientes().subscribe((data) => (this.categorias = data));
  }

  cargarProductos() {
    this.cargando = true;

    this.productoService
      .listarActivos(this.filtroNombre, this.filtroCategoria || undefined)
      .subscribe({
        next: (data: any[]) => {
          this.paquetes = data.map((p) => ({
            id: p.id,
            nombre: p.nombre,
            precio: Number(p.precio || p.price || 0),
            img: p.imagenUrl || '/img/placeholder.png',
            descripcion: p.descripcion,
          }));
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error cargando productos:', err);
          this.cargando = false;
        },
      });
  }

  buscar() {
    this.paginaActual = 1;
    this.cargarProductos();
  }

  filtrarPorCategoria(id: number | null) {
    this.filtroCategoria = id;
    this.paginaActual = 1;
    this.cargarProductos();
  }

  agregarAlCarrito(producto: CatalogoUI) {
    if (this.agregandoId === producto.id) return;

    this.agregandoId = producto.id;

    this.carritoService.agregarProducto(producto.id, 1).subscribe({
      next: () => {
        alert(`¡${producto.nombre} agregado al carrito! 🐝`);
        this.agregandoId = null;
      },
      error: (err) => {
        console.error(err);
        alert('No se pudo agregar (verifique stock o inicie sesión)');
        this.agregandoId = null;
      },
    });
  }

  /* ===========================
    PAGINACIÓN (Ya no filtramos precios aquí)
  =========================== */

  get paquetesFiltrados() {
    // Solo paginación, el filtro lo hizo el backend
    const inicio = (this.paginaActual - 1) * this.elementosPorPagina;
    return this.paquetes.slice(inicio, inicio + this.elementosPorPagina);
  }

  get paginas() {
    return Array.from(
      { length: Math.max(1, Math.ceil(this.paquetes.length / this.elementosPorPagina)) },
      (_, i) => i + 1
    );
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

  trackById(index: number, item: any): number {
    return item.id; // Angular solo actualizará si cambia el ID, no repintará todo
  }
}
