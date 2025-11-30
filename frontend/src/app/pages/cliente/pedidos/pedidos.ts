import { Component } from '@angular/core';
import { HeaderAdmin } from "../../../components/header-admin/header-admin.component";
import { Pedido } from '../../../interfaces/pedido';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pedidos',
  imports: [HeaderAdmin,CommonModule],
  templateUrl: './pedidos.html',
  styleUrl: './pedidos.css',
})
export class Pedidos {
pedidos: Pedido[] = [
    // { id: 1, cliente: 'Juan Perez', producto: 'Miel 1kg', cantidad: 2, total: 73, fecha: '2025-11-25', estado: 'Pendiente' },
    // { id: 2, cliente: 'Ana Diaz', producto: 'Propóleo', cantidad: 1, total: 30, fecha: '2025-11-24', estado: 'Pendiente' },
  ];
}
