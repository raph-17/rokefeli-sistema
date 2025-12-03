import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { HeaderAdmin } from '../../../components/header-admin/header-admin.component';
import { UbicacionService } from '../../../services/ubicacion.service';

// Servicios
import { ProductoService } from '../../../services/producto.service';
import { VentaService } from '../../../services/venta.service';
import { CategoriaService } from '../../../services/categoria.service';
import { AuthService } from '../../../services/auth.service';
import { AgenciaService } from '../../../services/agencia.service';
import { TarifaEnvioService } from '../../../services/tarifa-envio.service';
import { PedidoService, Pedido, EstadoPedido } from '../../../services/pedido.service';

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
  activeTab: 'productos' | 'inventario' | 'ventas' | 'ubicaciones' | 'envios' | 'pedidos' =
    'productos';
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

  // Filtro de ventas
  filtroVentaDni: string = '';
  filtroVentaEstado: string = ''; // '' = Todos
  filtroVentaCanal: string = ''; // '' = Todos

  // Lógica Venta Interna
  mostrarModalVenta = false;
  itemsVenta: any[] = []; // El "carrito" de la venta interna
  totalVentaInterna: number = 0;

  // Controles para agregar item
  productoSeleccionadoId: number | null = null;
  cantidadSeleccionada: number = 1;

  // Control para el cliente (ID del distribuidor o comprador local)
  idClienteInterno = new FormControl('', Validators.required);

  // --- VARIABLES UBICACIÓN ---
  // Nivel actual: 'DEPTO', 'PROV', 'DIST'
  nivelUbicacion: 'DEPTO' | 'PROV' | 'DIST' = 'DEPTO';

  listaUbicacion: any[] = []; // Aquí cargamos la tabla dinámica
  padreSeleccionado: any = null; // Guardamos el Depto o Prov actual para el título y el ID
  abueloSeleccionado: any = null; // Para volver atrás desde Distrito a Provincia

  nombreUbicacionControl = new FormControl('', Validators.required); // Input del modal

  // --- LÓGICA DE ENVÍOS ---
  agencias: any[] = [];
  tarifas: any[] = [];
  agenciaSeleccionada: any = null;

  // Modales
  mostrarModalAgencia = false;
  mostrarModalTarifa = false;

  // Formularios
  formAgencia = new FormGroup({
    nombre: new FormControl('', Validators.required),
  });

  formTarifa = new FormGroup({
    idAgencia: new FormControl(null, Validators.required),
    // Para los selects en cascada
    idDepartamento: new FormControl(null),
    idProvincia: new FormControl(null),
    idDistrito: new FormControl(null, Validators.required),
    // Datos de la tarifa
    costoEnvio: new FormControl(null, [Validators.required, Validators.min(0)]),
    diasEstimados: new FormControl(null, [Validators.required, Validators.min(1)]),
  });

  // Listas para selects en cascada
  deptosTarifa: any[] = [];
  provsTarifa: any[] = [];
  distsTarifa: any[] = []; // 'envios', 'productos', 'pedidos'

  // Datos de Pedidos
  pedidos: Pedido[] = [];
  pedidosFiltrados: Pedido[] = []; // Lista que se muestra en la tabla

  // Filtros
  filtroUsuario: string = ''; // Texto para buscar por nombre
  estadosPosibles = Object.values(EstadoPedido);

  // Modal Detalle
  mostrarModalPedido = false;
  pedidoSeleccionado: Pedido | null = null;

  // Variables para métricas
  metricsPedidos = { pendientes: 0, porDespachar: 0, enRuta: 0, totalDia: 0 };
  metricsEnvios = { rutasActivas: 0, costoProm: 0, tiempoProm: 0 };
  metricsUbicacion = { total: 0, activos: 0, inactivos: 0 };

  get inventarioFiltrado() {
    // Si el checkbox está marcado, filtramos
    if (this.mostrarSoloBajoStock) {
      // Aseguramos que p.stockMinimo tenga valor, si no asumimos 5 o 0
      return this.products.filter((p) => p.stockActual < (p.stockMinimo || 0));
    }
    // Si no, mostramos todo
    return this.products;
  }

  get agenciasActivasCount(): number {
    return this.agencias.filter((a) => a.estado === 'ACTIVO').length;
  }

  constructor(
    private productoService: ProductoService,
    private ventaService: VentaService,
    private categoriaService: CategoriaService,
    private authService: AuthService,
    private ubicacionService: UbicacionService,
    private agenciaService: AgenciaService,
    private tarifaService: TarifaEnvioService,
    private pedidoService: PedidoService
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
        this.productosActivos = this.products.filter((p) => p.estado === 'ACTIVO').length;
        this.productosInactivos = this.products.length - this.productosActivos;

        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
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
        this.allVentas = data; // Guardamos el TOTAL REAL
        this.ventasFiltradas = data; // Inicialmente mostramos todo

        this.calcularMetricasGlobales(); // Calculamos con allVentas
      },
      error: (err) => console.error(err),
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
    const ventasReales = this.allVentas.filter(
      (v) => v.estado === 'PAGADA' || v.estado === 'PROCESADA'
    );

    this.ventasTotales = this.allVentas.length; // Total histórico
    this.ingresosTotales = ventasReales.reduce((acc, v) => acc + (v.montoTotal || 0), 0);

    // Ventas Pendientes (Para alertar al admin)
    this.ventasProcesadas = this.allVentas.filter((v) => v.estado === 'PROCESADA').length;

    // Ticket Promedio (Cuánto gasta un cliente promedio)
    this.ticketPromedio = ventasReales.length > 0 ? this.ingresosTotales / ventasReales.length : 0;
  }

  // 1. LÓGICA PARA PEDIDOS
  // Llama a esta función dentro del subscribe de cargarPedidos()
  calcularMetricasPedidos() {
    const hoy = new Date().toDateString();

    this.metricsPedidos = {
      pendientes: this.pedidos.filter((p) => p.estado === 'PENDIENTE').length,
      porDespachar: this.pedidos.filter((p) => p.estado === 'EN_PREPARACION').length, // O el estado que uses para "Listo para enviar"
      enRuta: this.pedidos.filter((p) => p.estado === 'EN_REPARTO').length,
      totalDia: this.pedidos
        .filter((p) => new Date(p.fechaCreacion).toDateString() === hoy)
        .reduce((acc, curr) => acc + curr.total, 0),
    };
  }

  // 2. LÓGICA PARA ENVÍOS
  // Llama a esta función dentro del subscribe de verTarifas() (cuando seleccionas agencia)
  calcularMetricasEnvios() {
    if (!this.tarifas || this.tarifas.length === 0) {
      this.metricsEnvios = { rutasActivas: 0, costoProm: 0, tiempoProm: 0 };
      return;
    }

    // Solo calculamos promedios con las rutas activas para ser realistas
    const activas = this.tarifas.filter((t) => t.estado === 'ACTIVO');
    const count = activas.length;

    if (count === 0) {
      this.metricsEnvios = { rutasActivas: 0, costoProm: 0, tiempoProm: 0 };
      return;
    }

    const sumaCosto = activas.reduce((acc, t) => acc + t.costoEnvio, 0);
    const sumaDias = activas.reduce((acc, t) => acc + t.diasEstimados, 0);

    this.metricsEnvios = {
      rutasActivas: count,
      costoProm: sumaCosto / count,
      tiempoProm: sumaDias / count,
    };
  }

  cerrarDetalleAgencia() {
    this.agenciaSeleccionada = null;
    this.tarifas = [];
    this.metricsEnvios = { rutasActivas: 0, costoProm: 0, tiempoProm: 0 }; // Reset metrics
  }

  // 3. LÓGICA PARA UBICACIONES
  // Llama a esta función cada vez que cargues una lista (Deptos, Provs o Dists)
  calcularMetricasUbicacion() {
    this.metricsUbicacion = {
      total: this.listaUbicacion.length,
      activos: this.listaUbicacion.filter((u) => u.estado === 'ACTIVO').length,
      inactivos: this.listaUbicacion.filter((u) => u.estado !== 'ACTIVO').length,
    };
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
    this.ventaService
      .buscarAdmin(
        this.filtroVentaEstado || undefined,
        this.filtroVentaCanal || undefined,
        this.filtroVentaDni || undefined
      )
      .subscribe({
        next: (data) => {
          this.ventasFiltradas = data; // Solo cambiamos la tabla
          // NO llamamos a calcularMetricasGlobales() aquí, así que los Ingresos se mantienen fijos.
        },
        error: (err) => console.error(err),
      });
  }

  limpiarFiltrosVentas() {
    this.filtroVentaDni = '';
    this.filtroVentaEstado = '';
    this.filtroVentaCanal = '';
    this.filtrarVentas(); // Recarga todo
  }

  // --- VENTA INTERNA ---

  // 1. Abrir/Cerrar Modal
  abrirModalVenta() {
    this.itemsVenta = [];
    this.totalVentaInterna = 0;
    this.productoSeleccionadoId = null;
    this.cantidadSeleccionada = 1;
    this.idClienteInterno.setValue(''); // O pon un ID por defecto si quieres
    this.mostrarModalVenta = true;
  }

  cerrarModalVenta() {
    this.mostrarModalVenta = false;
  }

  // 2. Agregar producto a la lista temporal
  agregarItemVenta() {
    if (!this.productoSeleccionadoId || this.cantidadSeleccionada < 1) return;

    // Buscar producto real en la lista cargada
    const producto = this.products.find((p) => p.id == this.productoSeleccionadoId);

    if (!producto) return;

    // Validar Stock
    if (producto.stockActual < this.cantidadSeleccionada) {
      alert(`Solo quedan ${producto.stockActual} unidades de ${producto.nombre}`);
      return;
    }

    // Verificar si ya está en la lista para sumar cantidad
    const itemExistente = this.itemsVenta.find((i) => i.idProducto === producto.id);

    if (itemExistente) {
      itemExistente.cantidad += this.cantidadSeleccionada;
      itemExistente.subtotal = itemExistente.cantidad * itemExistente.precioUnitario;
    } else {
      // Nuevo item
      this.itemsVenta.push({
        idProducto: producto.id,
        nombre: producto.nombre,
        precioUnitario: producto.precioInterno || producto.precio, // Usa precio interno si existe
        cantidad: this.cantidadSeleccionada,
        subtotal: (producto.precioInterno || producto.precio) * this.cantidadSeleccionada,
      });
    }

    this.calcularTotalInterno();
    this.productoSeleccionadoId = null; // Resetear select
    this.cantidadSeleccionada = 1; // Resetear cantidad
  }

  eliminarItemVenta(index: number) {
    this.itemsVenta.splice(index, 1);
    this.calcularTotalInterno();
  }

  calcularTotalInterno() {
    this.totalVentaInterna = this.itemsVenta.reduce((acc, item) => acc + item.subtotal, 0);
  }

  // 3. Guardar Venta en Backend
  guardarVentaInterna() {
    if (this.itemsVenta.length === 0) {
      alert('Debe agregar al menos un producto');
      return;
    }
    if (this.idClienteInterno.invalid) {
      alert('Debe ingresar el ID del cliente/distribuidor');
      return;
    }

    // Armar el DTO como lo pide Java (VentaInternoCreateDTO)
    const dto = {
      idEmpleado: this.authService.getUserId(), // TODO: Sacar del Token del usuario logueado (ADMIN)
      idDistribuidor: this.idClienteInterno.value, // ID del usuario comprador
      detalles: this.itemsVenta.map((i) => ({
        idProducto: i.idProducto,
        cantidad: i.cantidad,
      })),
    };

    this.ventaService.registrarVentaInterno(dto).subscribe({
      // Asegúrate de tener este método en el service
      next: () => {
        alert('Venta interna registrada con éxito 💰');
        this.cargarDatosDashboard(); // Recargar stock y tabla
        this.cerrarModalVenta();
      },
      error: (err) => alert('Error al registrar: ' + err.message),
    });
  }

  // 1. CARGA INICIAL (DEPARTAMENTOS)
  cargarUbicaciones() {
    this.cargando = true;
    this.nivelUbicacion = 'DEPTO';
    this.padreSeleccionado = null;

    this.ubicacionService.listarDepartamentosAdmin().subscribe((data) => {
      this.listaUbicacion = data.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre));
      this.cargando = false;
      this.calcularMetricasUbicacion();
    });
  }

  // 2. NAVEGACIÓN (BAJAR DE NIVEL)
  verProvincias(depto: any) {
    this.cargando = true;
    this.nivelUbicacion = 'PROV';
    this.padreSeleccionado = depto;

    this.ubicacionService.listarProvinciasAdmin(depto.id).subscribe((data) => {
      this.listaUbicacion = data.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre));
      this.cargando = false;
      this.calcularMetricasUbicacion();
    });
  }

  verDistritos(provincia: any) {
    this.cargando = true;
    this.nivelUbicacion = 'DIST';
    this.abueloSeleccionado = this.padreSeleccionado;
    this.padreSeleccionado = provincia;

    this.ubicacionService.listarDistritosAdmin(provincia.id).subscribe((data) => {
      this.listaUbicacion = data.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre));
      this.cargando = false;
      this.calcularMetricasUbicacion();
    });
  }

  // 3. NAVEGACIÓN (SUBIR / VOLVER)
  volverNivel() {
    if (this.nivelUbicacion === 'DIST') {
      // Volver a Provincias
      this.verProvincias(this.abueloSeleccionado);
    } else if (this.nivelUbicacion === 'PROV') {
      // Volver a Departamentos
      this.cargarUbicaciones();
    }
  }

  // 4. GESTIÓN (ACTIVAR/DESACTIVAR)
  cambiarEstadoUbicacion(item: any, accion: 'activar' | 'desactivar') {
    let entidad: 'departamentos' | 'provincias' | 'distritos' = 'departamentos';
    if (this.nivelUbicacion === 'PROV') entidad = 'provincias';
    if (this.nivelUbicacion === 'DIST') entidad = 'distritos';

    this.ubicacionService.cambiarEstado(entidad, item.id, accion).subscribe(() => {
      // Recargar la lista actual
      if (this.nivelUbicacion === 'DEPTO') this.cargarUbicaciones();
      if (this.nivelUbicacion === 'PROV') this.verProvincias(this.padreSeleccionado);
      if (this.nivelUbicacion === 'DIST') this.verDistritos(this.padreSeleccionado);
    });
  }

  // 5. CREAR NUEVA UBICACIÓN
  mostrarModalUbicacion = false;

  abrirModalCrearUbicacion() {
    this.nombreUbicacionControl.reset();
    this.mostrarModalUbicacion = true;
  }

  cerrarModalUbicacion() {
    this.mostrarModalUbicacion = false;
  }

  guardarUbicacion() {
    if (this.nombreUbicacionControl.invalid) return;
    const nombre = this.nombreUbicacionControl.value || '';

    let obs;
    if (this.nivelUbicacion === 'DEPTO') {
      obs = this.ubicacionService.crearDepartamento(nombre);
    } else if (this.nivelUbicacion === 'PROV') {
      obs = this.ubicacionService.crearProvincia(nombre, this.padreSeleccionado.id);
    } else {
      obs = this.ubicacionService.crearDistrito(nombre, this.padreSeleccionado.id);
    }

    obs.subscribe({
      next: () => {
        alert('Registrado con éxito ✅');
        this.cerrarModalUbicacion();
        // Recargar vista actual
        if (this.nivelUbicacion === 'DEPTO') this.cargarUbicaciones();
        else if (this.nivelUbicacion === 'PROV') this.verProvincias(this.padreSeleccionado);
        else this.verDistritos(this.padreSeleccionado);
      },
      error: (err) => alert('Error: ' + err.message),
    });
  }

  // Actualiza cambiarTab para cargar datos si entran a 'ubicaciones'
  cambiarTab(tab: 'productos' | 'inventario' | 'ventas' | 'ubicaciones' | 'envios' | 'pedidos') {
    // Ajusta el tipo
    this.activeTab = tab;
    if (tab === 'ubicaciones') {
      this.cargarUbicaciones();
    }
    if (tab === 'envios') {
      this.cargarLogistica();
    }
  }

  // --- LÓGICA DE ENVÍOS ---
  // 1. Cargar lista de Agencias
  cargarLogistica() {
    this.cargando = true;
    this.agenciaService.findAll().subscribe((data) => {
      this.agencias = data.sort((a, b) => a.id - b.id);

      // Si teníamos una agencia seleccionada, buscamos su versión "actualizada" en la nueva lista
      if (this.agenciaSeleccionada) {
        const agenciaActualizada = this.agencias.find((a) => a.id === this.agenciaSeleccionada.id);

        if (agenciaActualizada) {
          this.agenciaSeleccionada = agenciaActualizada; // Actualizamos la referencia visual (el puntito de color)
          this.verTarifas(agenciaActualizada); // Forzamos recarga de las tarifas de la derecha
        } else {
          this.agenciaSeleccionada = null; // Si desapareció, limpiamos
          this.tarifas = [];
          this.cargando = false;
        }
      } else {
        this.cargando = false;
      }
    });
  }

  // 2. Seleccionar Agencia y ver sus tarifas
  verTarifas(agencia: any) {
    this.agenciaSeleccionada = agencia;
    this.cargando = true;
    this.tarifaService.listarPorAgencia(agencia.id).subscribe({
      next: (data) => {
        this.tarifas = data;
        this.cargando = false;
        this.calcularMetricasEnvios();
      },
      error: () => (this.cargando = false),
    });
  }

  // --- GESTIÓN AGENCIA ---

  guardarAgencia() {
    if (this.formAgencia.invalid) return;
    // DTO simple { nombre: "..." }
    this.agenciaService.create(this.formAgencia.value).subscribe(() => {
      alert('Agencia registrada ✅');
      this.mostrarModalAgencia = false;
      this.formAgencia.reset();
      this.cargarLogistica();
    });
  }

  eliminarAgencia(id: number) {
    if (!confirm('¿Eliminar esta agencia? Sus tarifas también se eliminarán.')) return;
    this.agenciaService.delete(id).subscribe(() => {
      this.agenciaSeleccionada = null;
      this.tarifas = [];
      this.cargarLogistica();
    });
  }

  desactivarTarifa(id: number) {
    if (!confirm('¿Desactivar esta tarifa?')) return;
    this.tarifaService.desactivar(id).subscribe(() => {
      // Recargamos solo la lista derecha para ser rápidos
      if (this.agenciaSeleccionada) this.verTarifas(this.agenciaSeleccionada);
    });
  }

  activarTarifa(id: number) {
    this.tarifaService.activar(id).subscribe(() => {
      if (this.agenciaSeleccionada) this.verTarifas(this.agenciaSeleccionada);
    });
  }

  toggleEstadoAgencia(agencia: any) {
    const accion = agencia.estado === 'ACTIVO' ? 'desactivar' : 'activar';

    if (
      accion === 'desactivar' &&
      !confirm('¿Desactivar agencia? Sus tarifas también se desactivarán.')
    )
      return;

    const obs =
      accion === 'desactivar'
        ? this.agenciaService.desactivar(agencia.id)
        : this.agenciaService.activar(agencia.id);

    obs.subscribe({
      next: () => {
        const idSeleccionado = this.agenciaSeleccionada?.id;

        // Recargamos todo
        this.cargarLogistica();
      },
      error: (err) => alert('Error al cambiar estado: ' + err.message),
    });
  }

  // --- GESTIÓN TARIFA ---

  abrirModalTarifa() {
    this.mostrarModalTarifa = true;
    this.formTarifa.reset();

    // Pre-llenar agencia si ya está seleccionada
    if (this.agenciaSeleccionada) {
      this.formTarifa.patchValue({ idAgencia: this.agenciaSeleccionada.id });
    }

    // Cargar Departamentos para iniciar la cascada
    this.ubicacionService
      .listarDepartamentosAdmin()
      .subscribe(
        (d) => (this.deptosTarifa = d.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre)))
      );
  }

  // Cascada: Al cambiar Depto -> Cargar Provincias
  onDeptoChange() {
    const id = this.formTarifa.value.idDepartamento;
    this.provsTarifa = [];
    this.distsTarifa = [];
    if (id) {
      this.ubicacionService
        .listarProvinciasAdmin(id)
        .subscribe(
          (p) => (this.provsTarifa = p.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre)))
        );
    }
  }

  // Cascada: Al cambiar Prov -> Cargar Distritos
  onProvChange() {
    const id = this.formTarifa.value.idProvincia;
    this.distsTarifa = [];
    if (id) {
      this.ubicacionService
        .listarDistritosAdmin(id)
        .subscribe(
          (d) => (this.distsTarifa = d.sort((a: any, b: any) => a.nombre.localeCompare(b.nombre)))
        );
    }
  }

  guardarTarifa() {
    // 1. Validaciones
    if (this.formTarifa.invalid) {
      this.formTarifa.markAllAsTouched();
      return;
    }

    if (!this.agenciaSeleccionada) {
      alert('Error: No hay una agencia seleccionada.');
      return;
    }

    // 2. Armar el DTO (Payload)
    const dto = {
      // CORRECCIÓN: Usamos el ID directo de la variable, no del formulario.
      // Esto garantiza que nunca sea null si el panel derecho está abierto.
      idAgenciaEnvio: this.agenciaSeleccionada.id,

      idDistrito: this.formTarifa.value.idDistrito,
      costoEnvio: this.formTarifa.value.costoEnvio,
      diasEstimados: this.formTarifa.value.diasEstimados,
    };

    console.log('Enviando Tarifa:', dto); // Para depurar si vuelve a fallar

    // 3. Enviar al Servicio
    this.tarifaService.create(dto).subscribe({
      next: () => {
        alert('Tarifa creada correctamente 🚚');
        this.mostrarModalTarifa = false;
        this.verTarifas(this.agenciaSeleccionada); // Recargar la lista
      },
      error: (e) => {
        console.error(e);
        alert('Error al crear tarifa: ' + (e.error?.message || e.message));
      },
    });
  }

  eliminarTarifa(id: number) {
    if (!confirm('¿Eliminar esta tarifa?')) return;
    this.tarifaService.delete(id).subscribe(() => this.verTarifas(this.agenciaSeleccionada));
  }

  // === LÓGICA DE PEDIDOS ===

  cargarPedidos() {
    this.pedidoService.listarTodos().subscribe({
      next: (data) => {
        this.pedidos = data;
        this.aplicarFiltros();
      },
      error: (err) => console.error('Error cargando pedidos', err),
    });
  }

  aplicarFiltros() {
    this.pedidosFiltrados = this.pedidos.filter((p) => {
      // 1. Filtro por Estado
      const cumpleEstado = this.filtroEstado === 'TODOS' || p.estado === this.filtroEstado;

      // 2. Filtro por Usuario (Nombre o Email)
      const busqueda = this.filtroUsuario.toLowerCase();
      const cumpleUsuario =
        !busqueda ||
        p.nombreUsuario.toLowerCase().includes(busqueda) ||
        p.emailUsuario.toLowerCase().includes(busqueda);

      return cumpleEstado && cumpleUsuario;
    });
  }

  // Accion rápida desde la tabla
  cambiarEstado(pedido: Pedido, nuevoEstado: string) {
    // string porque viene del select HTML
    const estadoEnum = nuevoEstado as EstadoPedido;

    if (confirm(`¿Cambiar pedido #${pedido.id} a ${estadoEnum}?`)) {
      this.pedidoService.cambiarEstado(pedido.id, estadoEnum).subscribe({
        next: (pedidoActualizado) => {
          // Actualizamos la lista local sin recargar todo
          const index = this.pedidos.findIndex((p) => p.id === pedido.id);
          if (index !== -1) this.pedidos[index] = pedidoActualizado;
          this.aplicarFiltros();

          // Si el modal está abierto, actualizamos también ahí
          if (this.pedidoSeleccionado?.id === pedido.id) {
            this.pedidoSeleccionado = pedidoActualizado;
          }
        },
      });
    }
  }

  verDetallePedido(pedido: Pedido) {
    // A veces el listado general no trae los detalles de productos (depende de tu DTO)
    // Hacemos una llamada para asegurar traer los items
    this.pedidoService.obtenerPorId(pedido.id).subscribe((p) => {
      this.pedidoSeleccionado = p;
      this.mostrarModalPedido = true;
    });
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

  compararCategorias(o1: any, o2: any): boolean {
    // Compara si son iguales (incluso si uno es string y otro number)
    // o si ambos son nulos/indefinidos
    return o1 == o2;
  }
}
