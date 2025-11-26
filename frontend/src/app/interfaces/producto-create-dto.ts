export interface ProductoCreateDTO {
nombre: string;
  descripcion: string;
  idCategoria: number;
  precio: number;
  precioInterno: number;
  stockActual: number;
  stockMinimo: number;
  imagenUrl: string;
}