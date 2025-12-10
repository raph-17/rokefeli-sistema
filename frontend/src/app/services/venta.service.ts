import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VentaService {
  // Recomiendo usar la URL completa para evitar problemas de proxy al inicio
  private apiUrl = 'http://localhost:8080/api/ventas';

  constructor(private http: HttpClient) {}

  /* ===========================
     CLIENTE
     =========================== */

  // Registrar una compra nueva (Checkout)
  registrarVentaOnline(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/online`, data);
  }

  // Ver historial de compras del cliente logueado
  listarMisCompras(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-compras`);
  }

  /* ===========================
     ADMIN
     =========================== */

  // Acepta filtros opcionales. Si son null/undefined, no se envían.
  buscarAdmin(estado?: string, canal?: string, dni?: string): Observable<any[]> {
    let params = new HttpParams();

    if (estado && estado !== '') params = params.set('estado', estado);
    if (canal && canal !== '') params = params.set('canal', canal);
    if (dni && dni !== '') params = params.set('dni', dni);

    // Llama al endpoint
    return this.http.get<any[]>(`${this.apiUrl}/admin/buscar`, { params });
  }

  // Listar todas las ventas sin filtro (para admin)
  listarAdmin(): Observable<any[]> {
    return this.buscarAdmin();
  }

  // Registrar Venta Interna
  registrarVentaInterno(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/interno`, data);
  }

  descargarReporte(idVenta: number): Observable<Blob> {
    // responseType: 'blob' es obligatorio para archivos
    return this.http.get(`${this.apiUrl}/${idVenta}/reporte`, { 
      responseType: 'blob' 
    });
  }
}
