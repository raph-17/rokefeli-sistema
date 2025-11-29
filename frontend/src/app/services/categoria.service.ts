import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  // Asegúrate de que este puerto coincida con tu backend (8080)
  private apiUrl = 'http://localhost:8080/api/categorias';

  constructor(private http: HttpClient) { }

  // Obtener todas las categorías (Para el filtro del Catálogo y el Select del Admin)
  findAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // (Opcional) Si necesitas buscar una específica por ID en el futuro
  findById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
}