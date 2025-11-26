import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private apiUrl = 'http://localhost:8080/api/carrito';

  // Observable para que el Header sepa cuántos items hay en tiempo real
  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();

  constructor(private http: HttpClient) {
    // Al iniciar, intentamos cargar la cantidad actual si hay usuario logueado
    this.actualizarContador();
  }

  // 1. Ver Carrito
  verCarrito(): Observable<any> {
    // No enviamos ID, el Interceptor pone el token y el Backend sabe quién es
    return this.http.get<any>(`${this.apiUrl}`).pipe(
      tap(carrito => this.actualizarSubject(carrito))
    );
  }

  // 2. Agregar Producto
  agregarProducto(idProducto: number, cantidad: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/agregar`, { idProducto, cantidad }).pipe(
      tap(carrito => this.actualizarSubject(carrito))
    );
  }

  // 3. Actualizar Cantidad
  actualizarCantidad(idProducto: number, cantidad: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/producto/${idProducto}?cantidad=${cantidad}`, {}).pipe(
      tap(carrito => this.actualizarSubject(carrito))
    );
  }

  // 4. Eliminar Item
  eliminarProducto(idProducto: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/producto/${idProducto}`).pipe(
      tap(carrito => this.actualizarSubject(carrito))
    );
  }

  // Helper para actualizar el contador del icono
  private actualizarSubject(carrito: any) {
    if (carrito && carrito.detalles) {
      // Sumar las cantidades de todos los items
      const totalItems = carrito.detalles.reduce((acc: number, item: any) => acc + item.cantidad, 0);
      this.cartCountSubject.next(totalItems);
    } else {
      this.cartCountSubject.next(0);
    }
  }
  
  // Llamar a esto al hacer Login para refrescar el icono
  public actualizarContador() {
    if (localStorage.getItem('token')) {
        this.verCarrito().subscribe({
            error: () => this.cartCountSubject.next(0) // Si falla (token vencido), resetear
        });
    }
  }

  // 5. Vaciar Carrito por completo
  vaciarCarrito(): Observable<any> {
    // Endpoint: DELETE /api/carrito/vaciar
    return this.http.delete<void>(`${this.apiUrl}/vaciar`).pipe(
      tap(() => {
        // Importante: Reseteamos el contador del icono a 0 inmediatamente
        this.cartCountSubject.next(0);
      })
    );
  }
}