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
  allVentas: any[] = [];
  ventasFiltradas: any[] = [];
  categorias: any[] = [];

  // Métricas
  ventasTotales: number = 0;
  ingresosTotales: number = 0;
  productosActivos: number = 0;
  productosInactivos: number = 0;
  ventasProcesadas: number = 0;
  ticketPromedio: number = 0;

  // Pestañas
  activeTab: 'productos' | 'inventario' | 'ventas' = 'productos';
  cargando = true;

  // Filtros
  filtroNombre: string = '';
  filtroCategoria: number | null = null; // O string vacío ''
  filtroEstado: string = '';
  mostrarSoloBajoStock: boolean = false;

  // Modal de stock
  mostrarModalStock = false;
  productoSeleccionado: any = null;
  cantidadStockControl = new FormControl<number | null>(null, [
    Validators.required,
    Validators.min(1),
  ]);

  // --- VARIABLES PARA FILTROS DE VENTAS ---
  filtroVentaDni: string = '';
  filtroVentaEstado: string = ''; // '' = Todos
  filtroVentaCanal: string = ''; // '' = Todos

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
        this.products = data.sort((a, b) => a.nombre.localeCompare(b.nombre));
        
        // Calculamos métricas de productos una sola vez
        this.productosActivos = this.products.filter(p => p.estado === 'ACTIVO').length;
        this.productosInactivos = this.products.length - this.productosActivos;
        
        this.cargando = false;
      },
      error: (err) => { console.error(err); this.cargando = false; }
    });

    // 2. Categorías
    this.categoriaService.findAll().subscribe({
      next: (data) => (this.categorias = data),
    });

    // 3. Ventas
    this.ventaService.listarAdmin().subscribe({
      next: (data) => {
        this.allVentas = data;      // Guardamos el TOTAL REAL
        this.ventasFiltradas = data; // Inicialmente mostramos todo
        
        this.calcularMetricasGlobales(); // Calculamos con allVentas
      },
      error: (err) => console.error(err)
    });

    this.filtrarVentas();
  }

  calcularMetricas() {
    // 1. Ventas Totales: Solo contamos las exitosas (PAGADA o PROCESADA)

    const ventasExitosas = this.ventasFiltradas.filter(
      (v) => v.estado === 'PAGADA' || v.estado === 'PROCESADA'
    );

    this.ventasTotales = this.ventasFiltradas.length; // Muestra total de intentos (o usa ventasExitosas.length si prefieres)

    // 2. Ingresos: Sumar solo las exitosas
    this.ingresosTotales = ventasExitosas.reduce((acc, v) => acc + (v.montoTotal || 0), 0);
  }

  calcularMetricasGlobales() {
    // Ventas Totales (Solo las pagadas/procesadas cuentan para dinero)
    const ventasReales = this.allVentas.filter(v => v.estado === 'PAGADA' || v.estado === 'PROCESADA');
    
    this.ventasTotales = this.allVentas.length; // Total histórico
    this.ingresosTotales = ventasReales.reduce((acc, v) => acc + (v.montoTotal || 0), 0);
    
    // Ventas Pendientes (Para alertar al admin)
    this.ventasProcesadas = this.allVentas.filter(v => v.estado === 'PROCESADA').length;

    // Ticket Promedio (Cuánto gasta un cliente promedio)
    this.ticketPromedio = ventasReales.length > 0 ? this.ingresosTotales / ventasReales.length : 0;
  }

  // --- MODAL: CREAR Y EDITAR ---

  abrirModalAgregarProducto() {
    this.idEdicion = null; // Modo Crear
    this.formProducto.reset({
      stockMinimo: null,
      precio: null,
      stockActual: null,
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

  // Modal stock
  abrirModalStock(producto: any) {
    this.productoSeleccionado = producto;
    this.cantidadStockControl.reset();
    this.mostrarModalStock = true;
  }

  cerrarModalStock() {
    this.mostrarModalStock = false;
    this.productoSeleccionado = null;
    this.cantidadStockControl.reset();
  }

  guardarAjusteStock(accion: 'SUMAR' | 'RESTAR') {
    if (!this.productoSeleccionado || this.cantidadStockControl.invalid) {
      this.cantidadStockControl.markAsTouched(); // Para mostrar error si está vacío
      return;
    }

    const valorIngresado = this.cantidadStockControl.value || 0;

    // Lógica matemática: Si es RESTAR, lo volvemos negativo
    const cantidadFinal =
      accion === 'RESTAR'
        ? -Math.abs(valorIngresado) // Asegura negativo
        : Math.abs(valorIngresado); // Asegura positivo

    const dto = {
      productoId: this.productoSeleccionado.id,
      cantidadCambio: cantidadFinal,
    };

    this.productoService.ajustarStock(dto).subscribe({
      next: () => {
        const verbo = accion === 'SUMAR' ? 'agregado' : 'retirado';
        alert(`Stock ${verbo} correctamente 📦`);
        this.cargarDatosDashboard();
        this.cerrarModalStock();
      },
      error: (err) => alert('Error ajustando stock: ' + err.message),
    });
  }

  // --- FILTRADO DE VENTAS ---
  filtrarVentas() {
    // Aquí llamamos al servicio como antes, PERO actualizamos 'ventasFiltradas'
    this.ventaService.buscarAdmin(
      this.filtroVentaEstado || undefined,
      this.filtroVentaCanal || undefined,
      this.filtroVentaDni || undefined
    ).subscribe({
      next: (data) => {
        this.ventasFiltradas = data; // Solo cambiamos la tabla
        // NO llamamos a calcularMetricasGlobales() aquí, así que los Ingresos se mantienen fijos.
      },
      error: (err) => console.error(err)
    });
  }

  limpiarFiltrosVentas() {
    this.filtroVentaDni = '';
    this.filtroVentaEstado = '';
    this.filtroVentaCanal = '';
    this.filtrarVentas(); // Recarga todo
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
