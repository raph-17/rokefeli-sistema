import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8080/api/productos';
  private http = inject(HttpClient);

  // MODIFICADO: Lógica de parámetros limpia
  listarActivos(nombre?: string, idCategoria?: number): Observable<any[]> {
    let params = new HttpParams();

    // Backend ya filtra por estado ACTIVO internamente, no lo enviamos.

    // Solo agregamos si hay valor real (no null, no undefined, no vacío)
    if (nombre && nombre.trim() !== '') {
      params = params.set('nombre', nombre);
    }
    
    // Solo agregamos si es un número válido
    if (idCategoria !== null && idCategoria !== undefined && idCategoria > 0) {
      params = params.set('idCategoria', idCategoria.toString());
    }

    // Si no hay params, el backend devolverá todos los activos
    return this.http.get<any[]>(`${this.apiUrl}/buscar`, { params });
  }

  /* ===========================
     ADMINISTRACIÓN
     =========================== */

  // Lista TODOS los productos (Activos y Descontinuados)
  listarAdmin(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  crearProducto(dto: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, dto);
  }

  actualizarProducto(id: number, dto: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, dto);
  }

  // Según tu Backend, el DELETE hace un "Soft Delete" (Descontinuar)
  desactivarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Para eliminar físicamente (si tu backend lo soporta) o alias de desactivar
  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // ⚠️ OJO: Necesitas crear este endpoint en tu ProductoController (Backend)
  // Actualmente tu backend solo tiene DELETE (desactivar).
  activarProducto(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/activar`, {});
  }
  
  // Gestión de Stock
  ajustarStock(productoId: number, cantidadCambio: number): Observable<void> {
    const body = { productoId, cantidadCambio };
    return this.http.patch<void>(`${this.apiUrl}/stock`, body);
  }
}