import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { HeaderAdmin } from '../../../components/header-admin/header-admin.component';

// Servicios
import { ProductoService } from '../../../services/producto.service';
import { VentaService } from '../../../services/venta.service';
import { CategoriaService } from '../../../services/categoria.service';

@Component({
  selector: 'app-panel-admin',
  standalone: true,
  imports: [HeaderAdmin, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './panel-admin.html',
  styleUrl: './panel-admin.css',
})
export class PanelAdmin implements OnInit {
  formProducto!: FormGroup;
  mostrarModal = false;

  // Estado de Edición (null = Creando, number = Editando ID)
  idEdicion: number | null = null;

  // Datos
  products: any[] = [];
  ventas: any[] = [];
  categorias: any[] = [];

  // Métricas
  ventasTotales: number = 0;
  ingresosTotales: number = 0;

  // Pestañas
  activeTab: 'productos' | 'inventario' | 'ventas' = 'productos';
  cargando = true;

  // Filtros
  filtroNombre: string = '';
  filtroCategoria: number | null = null; // O string vacío ''
  filtroEstado: string = '';
  mostrarSoloBajoStock: boolean = false;

  get inventarioFiltrado() {
    // Si el checkbox está marcado, filtramos
    if (this.mostrarSoloBajoStock) {
      // Aseguramos que p.stockMinimo tenga valor, si no asumimos 5 o 0
      return this.products.filter((p) => p.stockActual < (p.stockMinimo || 0));
    }
    // Si no, mostramos todo
    return this.products;
  }

  constructor(
    private productoService: ProductoService,
    private ventaService: VentaService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.cargarDatosDashboard();
    this.inicializarFormulario();
  }

  inicializarFormulario() {
    this.formProducto = new FormGroup({
      nombre: new FormControl('', Validators.required),
      descripcion: new FormControl('', Validators.required),
      idCategoria: new FormControl('', Validators.required),
      precio: new FormControl(0, [Validators.required, Validators.min(0)]),
      precioInterno: new FormControl(0, [Validators.min(0)]),
      stockActual: new FormControl(0, [Validators.required, Validators.min(0)]),
      stockMinimo: new FormControl(5, [Validators.required, Validators.min(0)]),
      imagenUrl: new FormControl(''),
    });
  }

  cargarDatosDashboard() {
    this.cargando = true;

    // 1. Productos
    this.productoService.listarAdmin().subscribe({
      next: (data) => {
        this.products = data;
        this.products.sort((a, b) => a.nombre.localeCompare(b.nombre));
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error productos:', err);
        this.cargando = false;
      },
    });

    // 2. Categorías
    this.categoriaService.findAll().subscribe({
      next: (data) => (this.categorias = data),
    });

    // 3. Ventas
    this.ventaService.listarAdmin().subscribe({
      next: (data) => {
        this.ventas = data;
        this.calcularMetricas();
      },
      error: (err) => console.error('Error ventas:', err),
    });
  }

  calcularMetricas() {
    this.ventasTotales = this.ventas.length;
    this.ingresosTotales = this.ventas.reduce((acc, v) => acc + (v.montoTotal || 0), 0);
  }

  // --- MODAL: CREAR Y EDITAR ---

  abrirModalAgregarProducto() {
    this.idEdicion = null; // Modo Crear
    this.formProducto.reset({
      stockMinimo: 5,
      precio: 0,
      stockActual: 0,
      idCategoria: '', // Reset select
    });
    this.mostrarModal = true;
  }

  abrirModalEditarProducto(producto: any) {
    this.idEdicion = producto.id;
    this.mostrarModal = true; // 1. Renderizamos el HTML (ngIf)

    // 2. Damos tiempo (aumentado a 100ms por seguridad)
    setTimeout(() => {
      const catId = Number(producto.categoriaId || producto.categoria?.id || producto.idCategoria);

      // --- DIAGNÓSTICO CRÍTICO ---
      console.log('--- INTENTANDO ASIGNAR ---');
      console.log('ID buscado:', catId, '(Tipo:', typeof catId, ')');
      console.log('Lista de Categorías en este momento:', this.categorias);

      const existe = this.categorias.find((c) => c.id === catId);
      console.log('¿Existe el ID en la lista?', existe ? 'SÍ' : 'NO');
      // ----------------------------

      // Llenamos el resto del formulario
      this.formProducto.patchValue({
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        precio: producto.precio,
        precioInterno: producto.precioInterno,
        stockActual: producto.stockActual,
        stockMinimo: producto.stockMinimo,
        imagenUrl: producto.imagenUrl,
        // idCategoria lo hacemos aparte abajo para asegurar
      });

      // Forzamos la asignación del control específico
      this.formProducto.get('idCategoria')?.setValue(catId);
    }, 100);
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.formProducto.reset();
    this.idEdicion = null;
  }

  // --- LÓGICA DE GUARDADO UNIFICADA ---
  guardarProducto() {
    if (this.formProducto.invalid) {
      this.formProducto.markAllAsTouched();
      return;
    }

    const datos = this.formProducto.value;

    if (this.idEdicion) {
      // ACTUALIZAR
      this.productoService.actualizarProducto(this.idEdicion, datos).subscribe({
        next: () => {
          alert('Producto actualizado correctamente ✅');
          this.cargarDatosDashboard();
          this.cerrarModal();
        },
        error: (err) => alert('Error al actualizar: ' + (err.error?.message || err.message)),
      });
    } else {
      // CREAR
      this.productoService.crearProducto(datos).subscribe({
        next: () => {
          alert('Producto creado con éxito ✅');
          this.cargarDatosDashboard();
          this.cerrarModal();
        },
        error: (err) => alert('Error al crear: ' + (err.error?.message || err.message)),
      });
    }
  }

  // --- FILTRADO ---

  filtrarProductos() {
    this.cargando = true;

    this.productoService
      .buscarAdmin(
        this.filtroNombre,
        this.filtroCategoria || undefined, // Enviamos undefined si es null/0
        this.filtroEstado || undefined // Enviamos undefined si es vacío
      )
      .subscribe({
        next: (data) => {
          this.products = data;
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error filtrando:', err);
          this.cargando = false;
        },
      });
  }

  limpiarFiltros() {
    this.filtroNombre = '';
    this.filtroCategoria = null;
    this.filtroEstado = '';
    this.cargarDatosDashboard(); // Vuelve a cargar la lista completa original
  }

  // --- OTRAS ACCIONES ---

  eliminarProducto(id: number) {
    if (!confirm('¿Estás seguro de eliminar este producto?')) return;
    this.productoService.eliminarProducto(id).subscribe(() => this.cargarDatosDashboard());
  }

  desactivarProducto(id: number) {
    if (!confirm('¿Desactivar producto del catálogo?')) return;
    this.productoService.desactivarProducto(id).subscribe(() => this.cargarDatosDashboard());
  }

  activarProducto(id: number) {
    this.productoService.activarProducto(id).subscribe(() => this.cargarDatosDashboard());
  }

  // --- UI ---

  get stockBajo() {
    return this.products.filter((p) => p.stockActual < p.stockMinimo).length;
  }

  cambiarTab(tab: 'productos' | 'inventario' | 'ventas') {
    this.activeTab = tab;
  }

  compararCategorias(o1: any, o2: any): boolean {
    // Compara si son iguales (incluso si uno es string y otro number)
    // o si ambos son nulos/indefinidos
    return o1 == o2;
  }
}
