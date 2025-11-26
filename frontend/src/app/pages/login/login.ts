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
import { AuthService } from '../../services/auth-service';
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
  const rol = data.rol;

  if (rol === "CLIENTE") {
    this.router.navigate(['/paquetes']);
  } else if (rol === "ADMIN" || rol === "EMPLEADO") {
    this.router.navigate(['/panel-admin']);
  }
},
    error: (err) => {
      console.error(err);
      alert("Credenciales incorrectas");
    }
  });
}
}