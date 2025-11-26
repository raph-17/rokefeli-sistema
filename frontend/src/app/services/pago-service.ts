import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PagoService {

  private api = '/api/pagos';

  constructor(private http: HttpClient) {}

  pagar(data: any): Observable<any> {
    return this.http.post(this.api, data);
  }
}