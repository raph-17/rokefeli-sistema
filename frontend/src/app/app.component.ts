import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router'; // 1. Importar Router
import { CommonModule } from '@angular/common'; // 2. Importar CommonModule (para *ngIf)
import { Header } from './components/header/header.component';
import { Footer } from './components/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  // 3. Agregar CommonModule a los imports
  imports: [RouterOutlet, Header, Footer, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'viaja-facil-frontend';

  // 4. Inyectar el Router como 'public' para usarlo en el HTML
  constructor(public router: Router) {}

  // 5. Método Helper para saber si es una página de Admin
  esRutaAdmin(): boolean {
    const url = this.router.url;
    // Si la URL incluye 'panel-admin' o 'admin', devolvemos true
    return url.includes('/panel-admin');
  }
}
