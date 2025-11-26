import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoCreateDTO } from '../../interfaces/producto-create-dto';

@Component({
  selector: 'app-modal-crear-producto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-crear-producto.html',
  styleUrls: ['./modal-crear-producto.css']
})
export class ModalCrearProductoComponent {

  @Output() cerrar = new EventEmitter<void>();
  @Output() crear = new EventEmitter<ProductoCreateDTO>();

  nuevoProducto: ProductoCreateDTO = {
    nombre: '',
    descripcion: '',
    idCategoria: 1,
    precio: 0,
    precioInterno: 0,
    stockActual: 0,
    stockMinimo: 0,
    imagenUrl: ''
  };

  enviar() {
    this.crear.emit(this.nuevoProducto);
  }

  cerrarModal() {
    this.cerrar.emit();
  }
}