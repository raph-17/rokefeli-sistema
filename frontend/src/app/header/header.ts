import { Component } from '@angular/core';
import { AuthService } from '../services/auth-service';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatAnchor } from "@angular/material/button";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatAnchor, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

  usuario: any = null;

  constructor(private authService: AuthService, private router: Router) {
    this.authService.usuarioActual$.subscribe(user => {
      this.usuario = user;
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}