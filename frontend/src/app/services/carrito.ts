import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private API_URL = 'http://localhost:8080/api/carrito';
  private KEY = 'carrito_local'; // respaldo local

  private carritoSubject = new BehaviorSubject<any[]>([]);
  carrito$ = this.carritoSubject.asObservable();

  constructor(private http: HttpClient) {
    this.sincronizarConBackend();
  }

  /** =====================================================================================
   *   1. OBTENER EL CARRITO REAL DEL BACKEND Y CARGARLO EN EL FRONT
   ===================================================================================== */
  sincronizarConBackend() {
    this.http.get<any>(this.API_URL).subscribe({
      next: res => {
        const items = res.items.map((i: any) => ({
          id: i.producto.id,
          nombre: i.producto.nombre,
          precio: i.producto.precio,
          img: i.producto.imagenUrl,
          cantidad: i.cantidad
        }));

        this.carritoSubject.next(items);
        localStorage.setItem(this.KEY, JSON.stringify(items));
      },
      error: () => {
        // si no hay login → usar localStorage solamente
        const local = JSON.parse(localStorage.getItem(this.KEY) || '[]');
        this.carritoSubject.next(local);
      }
    });
  }

  private actualizarEstado(data: any[]) {
    this.carritoSubject.next(data);
    localStorage.setItem(this.KEY, JSON.stringify(data));
  }

  getCarrito() {
    return [...this.carritoSubject.value];
  }

  /** =====================================================================================
   *   2. ACCIONES DEL CARRITO (estas llaman al BACKEND)
   ===================================================================================== */

  agregar(producto: any) {
    const dto = {
      idProducto: producto.id,
      cantidad: 1
    };

    this.http.post<any>(`${this.API_URL}/agregar`, dto).subscribe({
      next: res => this.sincronizarConBackend()
    });
  }

  sumar(id: number) {
    const item = this.getCarrito().find(p => p.id === id);
    if (!item) return;

    this.http.put(`${this.API_URL}/producto/${id}?cantidad=${item.cantidad + 1}`, {})
      .subscribe(() => this.sincronizarConBackend());
  }

  restar(id: number) {
    const item = this.getCarrito().find(p => p.id === id);
    if (!item) return;

    const nuevaCantidad = item.cantidad - 1;

    if (nuevaCantidad <= 0) {
      return this.eliminar(id);
    }

    this.http.put(`${this.API_URL}/producto/${id}?cantidad=${nuevaCantidad}`, {})
      .subscribe(() => this.sincronizarConBackend());
  }

  eliminar(id: number) {
    this.http.delete(`${this.API_URL}/producto/${id}`)
      .subscribe(() => this.sincronizarConBackend());
  }

  vaciarCarrito() {
    this.http.delete(`${this.API_URL}/vaciar`)
      .subscribe(() => {
        this.carritoSubject.next([]);
        localStorage.setItem(this.KEY, '[]');
      });
  }

  total() {
    return this.getCarrito()
      .reduce((acc, p) => acc + (p.precio * p.cantidad), 0);
  }
}