import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  // Asegúrate de que este puerto coincida con tu backend (8080)
  private apiUrl = 'http://localhost:8080/api/categorias';

  constructor(private http: HttpClient) { }

  // Obtener todas las categorías (Para el filtro del Catálogo y el Select del Admin)
  findAllClientes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Obtener todas las categorías para Admin
  findAllAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin`);
  }

  // (Opcional) Si necesitas buscar una específica por ID en el futuro
  findById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/${id}`);
  }

  // Crear categoria
  create(dto: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/admin`, dto);
  }

  // Actualizar categoria
  update(id: number, dto: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/admin/${id}`, dto);
  }

  // Descontinuar categoria
  desactivar(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/admin/${id}/desactivar`, {});
  }

  // Rehabilitar categoria
  activar(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/admin/${id}/activar`, {});
  }

  // Eliminar categoria
  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/admin/${id}`);
  }
}