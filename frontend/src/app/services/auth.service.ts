import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthResponse } from '../interfaces/auth-response';
import { jwtDecode } from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API_URL = 'http://localhost:8080/api/auth';

  // Estado global del usuario
  private usuarioActualSubject = new BehaviorSubject<any>(this.getUserFromToken());
  usuarioActual$ = this.usuarioActualSubject.asObservable();

  constructor(private http: HttpClient) {}

  registrarCliente(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, data);
  }

  login(data: { email: string; password: string }) {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, data);
  }

  // Guarda token + usuario
  guardarSesion(token: string) {
    localStorage.setItem("token", token);

    const user = this.getUserFromToken();
    this.usuarioActualSubject.next(user);
  }

  // Extrae usuario desde el token
  getUserFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      return jwtDecode(token);
    } catch {
      return null;
    }
  }

  // Cerrar sesión
  logout() {
    localStorage.removeItem("token");
    this.usuarioActualSubject.next(null);
  }

  // Saber si está logueado
  isLogged(): boolean {
    return localStorage.getItem("token") != null;
  }
}