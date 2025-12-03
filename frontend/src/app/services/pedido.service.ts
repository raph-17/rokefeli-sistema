import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// Asegúrate de que estos Enums coincidan con tu Backend Java
export enum EstadoPedido {
  PENDIENTE = 'PENDIENTE',
  EN_PREPARACION = 'EN_PREPARACION',
  EN_REPARTO = 'EN_REPARTO',
  ENTREGADO = 'ENTREGADO',
  CANCELADO = 'CANCELADO',
}

export interface Pedido {
  id: number;
  fechaCreacion: string;
  total: number;
  estado: EstadoPedido;
  nombreUsuario: string; // Asumo que el DTO de respuesta trae esto
  emailUsuario: string;
  detalles?: any[]; // Lista de productos del pedido
}

@Injectable({
  providedIn: 'root',
})
export class PedidoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/pedidos'; // Ajusta tu puerto si es necesario

  // === ADMIN ENDPOINTS ===

  // Listar todos (Admin)
  listarTodos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/admin`);
  }

  // Filtrar por Estado (Admin)
  listarPorEstado(estado: EstadoPedido): Observable<Pedido[]> {
    const params = new HttpParams().set('estado', estado);
    return this.http.get<Pedido[]>(`${this.apiUrl}/admin/estado`, { params });
  }

  // Cambiar Estado (PATCH)
  cambiarEstado(id: number, nuevoEstado: EstadoPedido): Observable<Pedido> {
    const params = new HttpParams().set('nuevoEstado', nuevoEstado);
    return this.http.patch<Pedido>(`${this.apiUrl}/admin/${id}/estado`, {}, { params });
  }

  // Obtener detalle completo (sirve para el modal)
  obtenerPorId(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/${id}`);
  }
}
