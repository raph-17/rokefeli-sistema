import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UbicacionService {
  private apiUrl = 'http://localhost:8080/api/ubicaciones';
  private http = inject(HttpClient);

  /* ===========================
     LECTURA (ADMIN - TRAE TODOS)
     =========================== */
  listarDepartamentos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/departamentos`);
  }

  listarProvincias(idDepto: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/provincias/departamento/${idDepto}`);
  }

  listarDistritos(idProv: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/distritos/provincia/${idProv}`);
  }

  /* ===========================
     LECTURA (ADMIN - TRAE TODOS)
     =========================== */
  listarDepartamentosAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/departamentos`);
  }

  listarProvinciasAdmin(idDepto: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/provincias/departamento/${idDepto}`);
  }

  listarDistritosAdmin(idProv: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/distritos/provincia/${idProv}`);
  }

  /* ===========================
     ESCRITURA (CREAR)
     =========================== */
  crearDepartamento(nombre: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/departamentos`, { nombre });
  }

  crearProvincia(nombre: string, idDepartamento: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/provincias`, { nombre, idDepartamento });
  }

  crearDistrito(nombre: string, idProvincia: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/distritos`, { nombre, idProvincia });
  }

  /* ===========================
     GESTIÓN DE ESTADOS
     =========================== */
  cambiarEstado(
    entidad: 'departamentos' | 'provincias' | 'distritos',
    id: number,
    accion: 'activar' | 'desactivar'
  ): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/admin/${entidad}/${id}/${accion}`, {});
  }
}
