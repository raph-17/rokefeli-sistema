export interface Productos {
      id: number;
  nombre: string;
  descripcion: string;
  categoria: {
    id: number;
    nombre: string;
  };
  precio: number;
  precioInterno: number;
  stockActual: number;
  stockMinimo: number;
  imagenUrl: string;
  estado: string; // depende de tu backend, algunos usan ACTIVO/INACTIVO
}