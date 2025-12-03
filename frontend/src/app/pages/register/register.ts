import { Component, signal, inject } from '@angular/core'; // Agregamos inject
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router'; // 1. IMPORTAR ROUTER Y ROUTERLINK

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    RouterLink, // 2. AGREGAR AQUI
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  hide = signal(true);
  registerForm: FormGroup;

  // Inyección de dependencias
  private authService = inject(AuthService);
  private router = inject(Router); // 3. INYECTAR ROUTER

  constructor() {
    this.registerForm = new FormGroup({
      Nombres: new FormControl('', Validators.required),
      Apellidos: new FormControl(''),
      CorreoElectronico: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      DNI: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(8),
        Validators.pattern('^[0-9]*$'), // Validación extra para solo números
      ]),
    });
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  handleSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const form = this.registerForm.value;

    const payload = {
      nombres: form.Nombres,
      apellidos: form.Apellidos,
      dni: form.DNI,
      email: form.CorreoElectronico,
      password: form.password,
    };

    this.authService.registrarCliente(payload).subscribe({
      next: (res) => {
        console.log('Registro exitoso', res);
        alert('Cuenta creada con éxito. Por favor inicia sesión.');

        // 4. REDIRECCIÓN EXITOSA
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Error en registro', err);
        alert('Error al registrar. Verifica si el correo o DNI ya existen.');
      },
    });
  }
}
