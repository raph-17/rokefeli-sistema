import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
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
    // Asumiendo que tienes un endpoint como /api/ventas/mis-compras 
    // o que filtras por usuario en el backend
    return this.http.get<any[]>(`${this.apiUrl}/mis-compras`);
  }

  /* ===========================
     ADMIN
     =========================== */

  // Este es el método que necesita tu PanelAdmin
  listarAdmin(): Observable<any[]> {
    // Llama al endpoint que devuelve TODAS las ventas
    // Tu backend debe tener un @GetMapping en VentaController para esto
    return this.http.get<any[]>(`${this.apiUrl}/admin`); 
  }
}