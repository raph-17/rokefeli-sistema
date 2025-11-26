import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';

// 1. Importamos 'withInterceptors'
import { provideHttpClient, withInterceptors } from '@angular/common/http';

// 2. Importamos la FUNCIÓN que creamos (asegúrate de la ruta correcta)
import { authInterceptor } from './core/auth.interceptor'; 

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),

    // 3. Configuración Moderna del Cliente HTTP con el Interceptor
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),
    provideAnimations()
  ]
};