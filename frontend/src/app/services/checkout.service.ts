import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CheckoutService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api';

  // 1. Ubicaciones (Rutas Corregidas según tu Controller)

  getDepartamentos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ubicaciones/departamentos`);
  }

  getProvincias(idDepto: number): Observable<any[]> {
    // CORREGIDO: /provincias/departamento/{id}
    return this.http.get<any[]>(`${this.apiUrl}/ubicaciones/provincias/departamento/${idDepto}`);
  }

  getDistritos(idProv: number): Observable<any[]> {
    // CORREGIDO: /distritos/provincia/{id}
    return this.http.get<any[]>(`${this.apiUrl}/ubicaciones/distritos/provincia/${idProv}`);
  }

  // 2. Tarifas disponibles para un distrito
  getTarifasPorDistrito(idDistrito: number): Observable<any[]> {
    // Base: /tarifas-envio
    // Endpoint: /distrito/{id}/disponibles
    return this.http.get<any[]>(`${this.apiUrl}/tarifas-envio/distrito/${idDistrito}/disponibles`);
  }

  // 3. Crear Venta Preliminar
  crearVentaPreliminar(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/ventas/crear`, {});
  }

  // 4. Procesar Pago
  procesarPago(payload: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/pagos`, payload);
  }
}
