import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VentaService {

  private api = '/api/ventas';

  constructor(private http: HttpClient) {}

  registrarVentaOnline(data: any): Observable<any> {
    return this.http.post(`${this.api}/online`, data);
  }
}