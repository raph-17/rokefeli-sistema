import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthResponse } from '../interfaces/auth-response';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
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
    localStorage.setItem('token', token);

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
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    this.usuarioActualSubject.next(null);
  }

  // Saber si está logueado
  isLogged(): boolean {
    return localStorage.getItem('token') != null;
  }

  // Método para obtener el ID del usuario logueado
  getUserId(): number | null {
    const token = localStorage.getItem('token');

    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);

      // TU CASO ESPECÍFICO: El ID está en 'sub'
      if (decoded.sub) {
        return Number(decoded.sub);
      }

      return null;
    } catch (error) {
      console.error('Error al decodificar token', error);
      return null;
    }
  }

  // Método extra útil: Obtener el Rol
  getRole(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.role || null;
    } catch (e) {
      return null;
    }
  }

  // Método útil para saber si es admin
  isAdmin(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;
    const decoded: any = jwtDecode(token);
    const rol = decoded.role || decoded.rol || ''; // Revisa si es 'role' o 'rol'
    return rol === 'ADMIN';
  }

  obtenerEmailDelToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';

    try {
      const payload = token.split('.')[1];
      const decodedString = atob(payload);
      const json = JSON.parse(decodedString);

      console.log('Contenido del Token:', json); // Para que veas en consola qué trae

      // 1. Buscamos explícitamente el campo 'email'
      if (json.email) return json.email;

      // 2. Si usamos 'sub', verificamos que parezca un correo (tenga @)
      // Si json.sub es "2", esto dará falso y no retornará basura
      if (json.sub && json.sub.toString().includes('@')) return json.sub;

      // 3. Intenta buscar 'username' si tu backend lo guarda así
      if (json.username && json.username.includes('@')) return json.username;

      return '';
    } catch (e) {
      return '';
    }
  }
}
