import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header-admin',
  imports: [RouterLink, MatButtonModule, CommonModule],
  templateUrl: './header-admin.html',
  styleUrl: './header-admin.css',
})
export class HeaderAdmin {
  usuario: any =null;
constructor(private authService: AuthService, private router: Router) {
  this.authService.usuarioActual$.subscribe(user => {
    this.usuario = user;
  });
}

logout() {
  this.authService.logout();
  this.router.navigate(['/login']);
}
}
