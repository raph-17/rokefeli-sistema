import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  // Signal para que los componentes sepan reactivamente el estado
  isDarkMode = signal<boolean>(false);

  constructor() {
    // Al iniciar, revisamos si el usuario ya tenía una preferencia guardada
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      this.activarModoOscuro();
    }
  }

  toggleTheme() {
    if (this.isDarkMode()) {
      this.activarModoClaro();
    } else {
      this.activarModoOscuro();
    }
  }

  private activarModoOscuro() {
    this.isDarkMode.set(true);
    document.body.classList.add('dark-mode');
    localStorage.setItem('theme', 'dark');
  }

  private activarModoClaro() {
    this.isDarkMode.set(false);
    document.body.classList.remove('dark-mode');
    localStorage.setItem('theme', 'light');
  }
}
