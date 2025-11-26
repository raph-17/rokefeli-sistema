export interface ProductoResponse {
    id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  precioInterno: number;
  stockActual: number;
  stockMinimo: number;
  imagenUrl: string;
  categoria: {
    id: number;
    nombre: string;
  };
  estado: string; // ACTIVO o DESCONTINUADO
}