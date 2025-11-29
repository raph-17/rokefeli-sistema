import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs'; // <--- Importante: 'of'

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8080/api/productos';
  private http = inject(HttpClient);

  // --- DATOS FALSOS PARA DISEÑO (MOCKS) ---
  private mockProductos = [
    { 
      id: 1, 
      nombre: 'Miel de Abeja 500g', 
      precio: 22.00, 
      stockActual: 50,
      imagenUrl: 'img/miel.jpg', // Asegúrate de tener alguna imagen o usará el placeholder
      descripcion: 'Miel pura multifloral cosechada en Ica.',
      categoria: { id: 1, nombre: 'Miel' },
      estado: 'ACTIVO'
    },
    { 
      id: 2, 
      nombre: 'Polen 100g', 
      precio: 15.00, 
      stockActual: 20,
      imagenUrl: 'img/polen.jpg', 
      descripcion: 'Energizante natural.',
      categoria: { id: 2, nombre: 'Polen' },
      estado: 'ACTIVO'
    },
    { 
      id: 3, 
      nombre: 'Jalea Real', 
      precio: 60.00, 
      stockActual: 5,
      imagenUrl: 'img/jalea.jpg', 
      descripcion: 'Alimento real puro.',
      categoria: { id: 3, nombre: 'Jalea' },
      estado: 'ACTIVO'
    },
    { 
      id: 4, 
      nombre: 'Propóleo en Gotas', 
      precio: 25.00, 
      stockActual: 30,
      imagenUrl: 'img/propoleo.jpg', 
      descripcion: 'Antibiótico natural.',
      categoria: { id: 4, nombre: 'Propóleo' },
      estado: 'ACTIVO'
    }
  ];

    /* ===========================
    MÉTODOS (Modo Mock Activado)
     =========================== */

  listarActivos(nombre?: string, idCategoria?: number): Observable<any[]> {
    // --- MODO REAL (Comentado por hoy) ---
    /*
    let params = new HttpParams();
    if (nombre) params = params.set('nombre', nombre);
    if (idCategoria) params = params.set('idCategoria', idCategoria.toString());
    return this.http.get<any[]>(`${this.apiUrl}/buscar`, { params });
    */

    // --- MODO MOCK (Activado) ---
    // Simulamos un filtro simple para que la barra de búsqueda funcione en el frontend
    let resultados = this.mockProductos;

    if (nombre) {
      resultados = resultados.filter(p => 
        p.nombre.toLowerCase().includes(nombre.toLowerCase())
      );
    }
    
    if (idCategoria) {
      resultados = resultados.filter(p => p.categoria.id === idCategoria);
    }

    return of(resultados); // 'of' convierte el array en un Observable
  }

  // Para el Admin
  listarAdmin(): Observable<any[]> {
    // return this.http.get<any[]>(this.apiUrl);
    return of(this.mockProductos);
  }

  obtenerPorId(id: number): Observable<any> {
    // return this.http.get<any>(`${this.apiUrl}/${id}`);
    const producto = this.mockProductos.find(p => p.id === id);
    return of(producto);
  }

  // Estos métodos no harán nada real en la BD, pero evitan errores en consola
  crearProducto(dto: any): Observable<any> {
    console.log('MOCK: Producto creado', dto);
    this.mockProductos.push({ ...dto, id: Math.random(), estado: 'ACTIVO' });
    return of(dto);
  }

  actualizarProducto(id: number, dto: any): Observable<any> {
    console.log('MOCK: Producto actualizado', id, dto);
    return of(dto);
  }

  desactivarProducto(id: number): Observable<void> {
    console.log('MOCK: Producto desactivado', id);
    return of(undefined);
  }

  activarProducto(id: number): Observable<void> {
    console.log('MOCK: Producto activado', id);
    return of(undefined);
  }
  
  eliminarProducto(id: number): Observable<void> {
    console.log('MOCK: Producto eliminado', id);
    return of(undefined);
  }
}