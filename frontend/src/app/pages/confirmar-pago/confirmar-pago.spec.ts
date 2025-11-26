import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmarPagoComponent } from './confirmar-pago'; // Asegúrate que el nombre de la clase coincida
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('ConfirmarPagoComponent', () => {
  let component: ConfirmarPagoComponent;
  let fixture: ComponentFixture<ConfirmarPagoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Importamos el componente standalone y el módulo de animaciones (para Material)
      imports: [ConfirmarPagoComponent, NoopAnimationsModule],
      
      // Proveemos las dependencias que el componente pide en su constructor
      providers: [
        provideHttpClient(), // Para que funcionen los servicios (Carrito, Venta, Pago)
        provideHttpClientTesting(), // Para simular las peticiones (evita errores de red real)
        provideRouter([]) // Para el Router
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmarPagoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});