import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { CarritoService } from '../../services/carrito.service';
import { AuthService } from '../../services/auth.service'; // Ajusta la ruta si es necesario

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class Header {
  
  carritoService = inject(CarritoService);
  authService = inject(AuthService);
  router = inject(Router);

  // Usamos el Observable directamente con el pipe async en el HTML
  cantidadItems$ = this.carritoService.cartCount$;
  
  // Verifica si hay token para mostrar "Login" o "Logout"
  estoyLogueado(): boolean {
    return !!localStorage.getItem('token'); // O usa tu authService.isLoggedIn()
  }

  logout() {
    // Si tu authService tiene logout, úsalo. Si no, limpiamos a mano:
    this.authService.logout(); // O localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}