export interface Usuario {
    id: number;
  nombre: string;
  email: string;
  tipo: 'Cliente' | 'Empleado' | 'Administrador';
}
