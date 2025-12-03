import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Carrito } from '../models/carrito.model'; // Asegúrate de importar la interfaz

@Injectable({
  providedIn: 'root',
})
export class CarritoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/carrito';

  // Fuente de verdad del contador
  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();

  constructor() {
    this.actualizarContador();
  }

  // 1. Ver Carrito
  verCarrito(): Observable<Carrito> {
    return this.http
      .get<Carrito>(this.apiUrl)
      .pipe(tap((carrito) => this.actualizarSubject(carrito)));
  }

  // 2. Agregar Producto
  agregarProducto(idProducto: number, cantidad: number): Observable<Carrito> {
    // El body debe coincidir con DetalleCarritoCreateDTO de Java
    const body = { idProducto, cantidad };
    return this.http
      .post<Carrito>(`${this.apiUrl}/agregar`, body)
      .pipe(tap((carrito) => this.actualizarSubject(carrito)));
  }

  // 3. Actualizar Cantidad
  // Java espera: @RequestParam Integer cantidad
  actualizarCantidad(idProducto: number, cantidad: number): Observable<Carrito> {
    const params = new HttpParams().set('cantidad', cantidad);

    return this.http
      .put<Carrito>(`${this.apiUrl}/producto/${idProducto}`, {}, { params })
      .pipe(tap((carrito) => this.actualizarSubject(carrito)));
  }

  // 4. Eliminar Item
  eliminarProducto(idProducto: number): Observable<Carrito> {
    return this.http
      .delete<Carrito>(`${this.apiUrl}/producto/${idProducto}`)
      .pipe(tap((carrito) => this.actualizarSubject(carrito)));
  }

  // 5. Vaciar Carrito
  // Java devuelve void, así que no esperamos un objeto Carrito de vuelta
  vaciarCarrito(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/vaciar`).pipe(
      tap(() => {
        this.cartCountSubject.next(0); // Reseteo manual porque no vuelve nada
      })
    );
  }

  // --- LÓGICA DE CONTADOR ---

  private actualizarSubject(carrito: Carrito) {
    if (carrito && carrito.detalles) {
      // Sumamos la cantidad de items (no solo filas, sino cantidad total)
      const total = carrito.detalles.reduce((acc, item) => acc + item.cantidad, 0);
      this.cartCountSubject.next(total);
    } else {
      this.cartCountSubject.next(0);
    }
  }

  public actualizarContador() {
    if (this.isLoggedIn()) {
      this.verCarrito().subscribe({
        error: () => this.cartCountSubject.next(0),
      });
    } else {
      this.cartCountSubject.next(0);
    }
  }

  private isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
