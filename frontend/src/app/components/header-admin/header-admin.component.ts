import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Ajusta la ruta

@Component({
  selector: 'app-header-admin',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header-admin.component.html',
  styleUrl: './header-admin.component.css',
})
export class HeaderAdmin {
  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    // Asumiendo que tienes un método logout en tu servicio, si no: localStorage.clear()
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
