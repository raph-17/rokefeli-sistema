import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductoService } from '../../services/producto.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class Home implements OnInit {

  destacados: any[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit() {
    // Usamos el servicio (que ahora devuelve mocks)
    this.productoService.listarActivos().subscribe(data => {
      // Tomamos solo los 3 primeros para el home
      this.destacados = data.slice(0, 3);
    });
  }
}