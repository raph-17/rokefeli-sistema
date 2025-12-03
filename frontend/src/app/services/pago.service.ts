import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PagoCreateDTO {
  idVenta: number;
  idTarifaEnvio: number;
  monto: number;
  emailCliente: string;
  tokenCulqi: string; // El token que nos da Culqi.js
}

@Injectable({
  providedIn: 'root',
})
export class PagoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pagos';

  realizarPago(dto: PagoCreateDTO): Observable<any> {
    return this.http.post<any>(this.apiUrl, dto);
  }
}
