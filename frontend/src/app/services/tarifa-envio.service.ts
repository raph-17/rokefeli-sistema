import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TarifaEnvioService {
  private apiUrl = 'http://localhost:8080/api/tarifas-envio';
  private http = inject(HttpClient);

  // Listar tarifas de una agencia específica
  listarPorAgencia(idAgencia: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/agencia/${idAgencia}`);
  }

  // Crear Tarifa
  create(dto: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, dto);
  }

  // Eliminar Tarifa
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/${id}`);
  }

  // Desactivar Tarifa
  desactivar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/admin/${id}/desactivar`, {});
  }

  // Activar Tarifa
  activar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/admin/${id}/activar`, {});
  }
}
