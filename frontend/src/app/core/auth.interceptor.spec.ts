import { TestBed } from '@angular/core/testing';
import { HttpClient, HttpInterceptorFn, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { authInterceptor } from './auth.interceptor'; // <--- Asegúrate de que la ruta sea correcta

describe('authInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        // 1. Configuramos el cliente HTTP con TU interceptor funcional
        provideHttpClient(withInterceptors([authInterceptor])),
        // 2. Habilitamos el Testing Controller para interceptar las llamadas
        provideHttpClientTesting()
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    
    // Limpiamos el localStorage antes de cada prueba
    localStorage.clear();
  });

  afterEach(() => {
    // Verifica que no queden peticiones pendientes
    httpMock.verify();
    localStorage.clear();
  });

  it('debería agregar el header Authorization si existe un token', () => {
    // A. Preparamos el escenario (Simulamos que hay token)
    const tokenPrueba = 'token-falso-123';
    localStorage.setItem('token', tokenPrueba);

    // B. Hacemos una petición de prueba
    httpClient.get('/api/test').subscribe();

    // C. Interceptamos la petición que salió
    const req = httpMock.expectOne('/api/test');

    // D. VERIFICACIÓN: ¿Tiene el header?
    expect(req.request.headers.has('Authorization')).toBeTrue();
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${tokenPrueba}`);
    
    req.flush({}); // Respondemos para cerrar la petición
  });

  it('NO debería agregar el header Authorization si NO hay token', () => {
    // A. Preparamos el escenario (Sin token)
    localStorage.removeItem('token');

    // B. Hacemos una petición de prueba
    httpClient.get('/api/test').subscribe();

    // C. Interceptamos la petición
    const req = httpMock.expectOne('/api/test');

    // D. VERIFICACIÓN: ¿Está limpio el header?
    expect(req.request.headers.has('Authorization')).toBeFalse();
    
    req.flush({});
  });
});