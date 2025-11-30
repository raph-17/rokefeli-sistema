import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos';
  private http = inject(HttpClient);

  /* ===========================
    MÉTODOS DE LECTURA (GET)
  =========================== */

  // Lista productos activos para el cliente (Catálogo)
  listarActivos(nombre?: string, idCategoria?: number): Observable<any[]> {
    let params = new HttpParams();

    if (nombre) params = params.set('nombre', nombre);
    if (idCategoria) params = params.set('idCategoria', idCategoria.toString());

    return this.http.get<any[]>(`${this.apiUrl}/buscar`, { params });
  }

  // Obtener detalle por ID
  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Lista TODOS los productos para el Admin (Activos y Descontinuados)
  listarAdmin(): Observable<any[]> {
    // Llama al endpoint que devuelve todo sin filtrar estado
    return this.http.get<any[]>(`${this.apiUrl}/admin`);
  }

  // Filtrar para admin
  buscarAdmin(nombre?: string, idCategoria?: number, estado?: string): Observable<any[]> {
    let params = new HttpParams();

    if (nombre) params = params.set('nombre', nombre);
    // Convertimos a string solo si existe
    if (idCategoria) params = params.set('idCategoria', idCategoria.toString());
    if (estado) params = params.set('estado', estado);

    return this.http.get<any[]>(`${this.apiUrl}/admin/buscar`, { params });
  }

  // Obtener detalle por ID para ADMIN
  obtenerPorIdAdmin(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/${id}`);
  }

  /* ===========================
    MÉTODOS DE ESCRITURA (ADMIN)
  =========================== */

  // Crear nuevo producto
  crearProducto(dto: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, dto);
  }

  // Actualizar producto existente
  actualizarProducto(id: number, dto: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, dto);
  }

  // Desactivar producto
  desactivarProducto(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/desactivar`, {});
  }

  // Reactivar producto
  activarProducto(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/activar`, {});
  }

  // Ajustar stock
  ajustarStock(dto: any): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/stock/ajustar`, dto);
  }

  // Eliminar (Hard Delete)
  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
