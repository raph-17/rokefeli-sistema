import { Component, OnInit } from '@angular/core';
import { HeaderAdmin } from "../../components/header-admin/header-admin";
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { ProductoResponse } from '../../interfaces/producto-response';
import { ProductoCreateDTO } from '../../interfaces/producto-create-dto';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VentaService } from '../../services/venta.service';

@Component({
  selector: 'app-panel-admin',
  standalone: true,
  imports: [HeaderAdmin, CommonModule, ReactiveFormsModule],
  templateUrl: './panel-admin.html',
  styleUrl: './panel-admin.css',
})
export class PanelAdmin implements OnInit {
  formProducto!: FormGroup;
  
  mostrarModal = false;
  ventas: any[] = [];
ventasTotales: number = 0;
ingresosTotales: number = 0;


  activeTab: 'productos' | 'inventario' | 'ventas' = 'productos';

  products: ProductoResponse[] = [];
  cargando = true;

  // Umbral para inventario
  minStock = 10;

  constructor(private productoService: ProductoService, private ventaService: VentaService) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarVentas();

    this.formProducto = new FormGroup({
    nombre: new FormControl('', Validators.required),
    descripcion: new FormControl('', Validators.required),
    idCategoria: new FormControl(1, Validators.required),
    precio: new FormControl(0, Validators.required),
    precioInterno: new FormControl(0, Validators.required),
    stockActual: new FormControl(0, Validators.required),
    stockMinimo: new FormControl(0, Validators.required),
    imagenUrl: new FormControl('', Validators.required)
  });
  }
abrirModalAgregarProducto() {
  this.mostrarModal = true;
}
cerrarModal() {
  this.mostrarModal = false;
  this.formProducto.reset({
    idCategoria: 1,
    precio: 0,
    precioInterno: 0,
    stockActual: 0,
    stockMinimo: 0
  });
}
  cargarProductos() {
    this.cargando = true;
    this.productoService.listarAdmin().subscribe({
      next: (data) => {
        this.products = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error listando productos:', err);
        this.cargando = false;
      }
    });
  }

  cargarVentas() {
    this.ventaService.listarAdmin().subscribe({ // Asume un método listarAdmin o findAll
      next: (data: any[]) => {
        this.ventas = data;
        
        // Calcular métricas automáticamente
        this.ventasTotales = this.ventas.length;
        this.ingresosTotales = this.ventas.reduce((acc, venta) => acc + venta.total, 0);
      },
      error: (err) => console.error('Error cargando ventas', err)
    });
  }

  /* ===========================
       GESTIÓN DE PRODUCTOS
     =========================== */

  agregarProducto() {
  if (this.formProducto.invalid) {
    this.formProducto.markAllAsTouched();
    return;
  }

  const nuevo: ProductoCreateDTO = this.formProducto.value;

  this.productoService.crearProducto(nuevo).subscribe({
    next: () => {
      this.cargarProductos();
      this.cerrarModal(); // cerramos modal
      this.formProducto.reset(); // limpiamos form
    },
    error: err => console.error("Error al crear producto:", err)
  });
}

  eliminarProducto(id: number) {
    if (!confirm("¿Eliminar producto?")) return;

    this.productoService.eliminarProducto(id).subscribe({
      next: () => this.cargarProductos(),
      error: (err) => console.error("Error al eliminar:", err)
    });
  }

  desactivarProducto(id: number) {
    this.productoService.desactivarProducto(id).subscribe({
      next: () => this.cargarProductos(),
      error: (err) => console.error("Error al desactivar:", err)
    });
  }

  activarProducto(id: number) {
    this.productoService.activarProducto(id).subscribe({
      next: () => this.cargarProductos(),
      error: (err) => console.error("Error al activar:", err)
    });
  }

  /* ===========================
          MÉTRICAS / TABS
     =========================== */

  get stockBajo() {
    return this.products.filter(p => p.stockActual < this.minStock).length;
  }

  cambiarTab(tab: 'productos' | 'inventario' | 'ventas') {
    this.activeTab = tab;
  }
}