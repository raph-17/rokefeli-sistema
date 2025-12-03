import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AgenciaService {
  private apiUrl = 'http://localhost:8080/api/agencias';
  private http = inject(HttpClient);

  // Admin: Listar TODAS (Activas e Inactivas)
  findAll(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin`);
  }

  // Crear (DTO: { nombre: "Olva" })
  create(dto: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, dto);
  }

  // Actualizar
  update(id: number, dto: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/${id}`, dto);
  }

  // Activar / Desactivar
  activar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/admin/${id}/activar`, {});
  }

  desactivar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/admin/${id}/desactivar`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/${id}`);
  }
}
