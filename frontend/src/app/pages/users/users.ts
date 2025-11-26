import { Component } from '@angular/core';
import { Usuario } from '../../interfaces/usuario';
import { CommonModule } from '@angular/common';
import { HeaderAdmin } from '../../components/header-admin/header-admin';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-users',
  standalone: true,   // ✅ ¡IMPORTANTE!
  imports: [CommonModule, HeaderAdmin, ReactiveFormsModule],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class Users {

  activeTab: 'clientes' | 'empleados' | 'administradores' = 'clientes';

  usuarios: Usuario[] = [
    { id: 1, nombre: 'Juan Pérez', email: 'juan@gmail.com', tipo: 'Cliente' },
    { id: 2, nombre: 'Ana Ruiz', email: 'ana@empresa.com', tipo: 'Empleado' },
    { id: 3, nombre: 'Luis Castro', email: 'admin@site.com', tipo: 'Administrador' }
  ];

  get filteredUsers() {
    const mapTipoToTab: Record<string, string> = {
      'Cliente': 'clientes',
      'Empleado': 'empleados',
      'Administrador': 'administradores'
    };

    return this.usuarios.filter(u => mapTipoToTab[u.tipo] === this.activeTab);
  }

  showModal = false;
  mostrarPassword = false;

  // DECLARAMOS el formulario pero NO usamos fb aún
  formRegistro!: FormGroup;

  constructor(private fb: FormBuilder) {
    // ✅ AHORA fb ya existe → seguro inicializar aquí
    this.formRegistro = this.fb.group({
      nombre: [''],
      apellido: [''],
      email: [''],
      password: [''],
      tipo: [false] // false = empleado, true = administrador
    });
  }

  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  registrarUsuario() {
    const data = this.formRegistro.value;

    const nuevoUsuario = {
      nombre: data.nombre,
      apellido: data.apellido,
      email: data.email,
      password: data.password,
      tipo: data.tipo ? 'Administrador' : 'Empleado'
    };

    console.log('Creando usuario:', nuevoUsuario);

    this.closeModal();
  }
}