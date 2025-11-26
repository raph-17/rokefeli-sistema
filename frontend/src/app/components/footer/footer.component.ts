import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class Footer {
  // Este componente es solo visual, no requiere lógica por ahora.
  // Si quisieras poner el año dinámico (2025, 2026...), lo harías aquí:
  currentYear = new Date().getFullYear();
}