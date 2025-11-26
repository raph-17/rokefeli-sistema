import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PagoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pagos';

  pagar(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }
}