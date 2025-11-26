import { Component, signal } from '@angular/core';
import { Header } from "../../header/header";
import { Footer } from "../../footer/footer";
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { jwtDecode } from "jwt-decode";

@Component({
  selector: 'app-login',
  imports: [
    Header,
    Footer,
    CommonModule,
    FormsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
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
    password: new FormControl('', Validators.required)
  });
}

handleSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const payload = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };

    this.authService.login(payload).subscribe({
      next: (res) => {
        this.authService.guardarSesion(res.token);

        const data: any = jwtDecode(res.token);
        
        // 🔍 AGREGA ESTAS LÍNEAS PARA DEPURAR
        console.log('Token completo decodificado:', data);
        console.log('Valor de rol intentando leer:', data.rol); 
        // -------------------------------------

        // A veces la propiedad se llama 'role', 'roles' o 'authorities'
        // A veces viene como array ["ADMIN"] en lugar de string "ADMIN"
        
        // Vamos a intentar leerlo de varias formas comunes:
        let rol = data.rol || data.role || data.roles || data.authorities;

        // Si el rol viene en un array (ej: ["ADMIN"]), tomamos el primero
        if (Array.isArray(rol)) {
            rol = rol[0];
        }

        console.log('Rol final procesado:', rol);

        // Lógica de redirección
        if (rol === "CLIENTE" || rol === "ROLE_CLIENTE") {
          console.log('Redirigiendo a paquetes...');
          this.router.navigate(['/paquetes']);
        } else if (rol === "ADMIN" || rol === "EMPLEADO" || rol === "ROLE_ADMIN") {
          console.log('Redirigiendo a admin...');
          this.router.navigate(['/panel-admin']);
        } else {
            console.warn('No se reconoció el rol para redirigir:', rol);
        }
      },
      error: (err) => {
        console.error(err);
        alert("Credenciales incorrectas");
      }
    });
  }
};