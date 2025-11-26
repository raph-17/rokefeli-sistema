import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// Si tienes interfaces definidas, úsalas. Si no, puedes cambiar a <any>
// import { ProductoResponse } from '../interfaces/producto-response';
// import { ProductoCreateDTO } from '../interfaces/producto-create-dto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8080/api/productos';
  
  private http = inject(HttpClient);

  /* ===========================
     PÚBLICO (Catálogo)
     =========================== */

  // Lista solo los productos que el cliente puede comprar
  // Llama a /api/productos/buscar?estado=ACTIVO
  listarActivos(): Observable<any[]> {
    const params = new HttpParams().set('estado', 'ACTIVO');
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