import { Component, signal } from '@angular/core';
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

@Component({
  selector: 'app-register',
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
    MatIconModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  hide = signal(true);

  registerForm: FormGroup;

  constructor(private authService: AuthService) {

    this.registerForm = new FormGroup({
      Nombres: new FormControl('', Validators.required),
      Apellidos: new FormControl(''),
      CorreoElectronico: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      DNI: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(8)
      ])
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
    password: form.password
  };

  this.authService.registrarCliente(payload).subscribe({
    next: (res) => {
      console.log("Registro exitoso", res);
      alert("Cliente registrado correctamente");
    },
    error: (err) => {
      console.error("Error en registro", err);
      alert("Error al registrar cliente");
    }
  });
}
}