import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    RouterLink,
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  hide = signal(true);

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  loginForm: FormGroup;

  constructor(private authService: AuthService, private router: Router) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
    });
  }

  handleSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const payload = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password,
    };

    this.authService.login(payload).subscribe({
      next: (res) => {
        this.authService.guardarSesion(res.token);

        const data: any = jwtDecode(res.token);

        console.log('Token completo decodificado:', data);

        let rol = data.rol || data.role || data.roles || data.authorities;

        if (Array.isArray(rol)) {
          rol = rol[0];
        }

        console.log('Rol final procesado:', rol);

        // Lógica de redirección
        if (rol === 'CLIENTE' || rol === 'ROLE_CLIENTE') {
          this.router.navigate(['/catalogo']);
        } else if (rol === 'ADMIN' || rol === 'EMPLEADO' || rol === 'ROLE_ADMIN') {
          this.router.navigate(['/panel-admin']);
        } else {
          console.warn('No se reconoció el rol para redirigir:', rol);
          this.router.navigate(['/catalogo']);
        }
      },
      error: (err) => {
        console.error(err);
        alert('Credenciales incorrectas');
      },
    });
  }
}
