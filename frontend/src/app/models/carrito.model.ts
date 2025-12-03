export interface DetalleCarrito {
  id: number;
  idProducto: number;
  nombreProducto: string;
  imagenUrl: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface Carrito {
  id: number;
  idUsuario: number;
  total: number;
  detalles: DetalleCarrito[];
}
