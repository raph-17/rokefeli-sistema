import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductoResponse } from '../interfaces/producto-response';
import { ProductoCreateDTO } from '../interfaces/producto-create-dto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private API_URL = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  // GET /api/productos  (solo activos)
  listarActivos(): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(this.API_URL);
  }

  // GET /api/productos/buscar?nombre=x&idCategoria=y
  buscar(nombre?: string, idCategoria?: number): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(`${this.API_URL}/buscar`, {
      params: {
        nombre: nombre || '',
        idCategoria: idCategoria?.toString() || ''
      }
    });
  }
listarAdmin(): Observable<ProductoResponse[]> {
    return this.http.get<ProductoResponse[]>(`${this.API_URL}/admin`);
  }

  crearProducto(data: ProductoCreateDTO): Observable<ProductoResponse> {
    return this.http.post<ProductoResponse>(`${this.API_URL}`, data);
  }

  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  desactivarProducto(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/desactivar`, {});
  }

  activarProducto(id: number): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/activar`, {});
  }
}