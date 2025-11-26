import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmarPago } from './confirmar-pago';

describe('ConfirmarPago', () => {
  let component: ConfirmarPago;
  let fixture: ComponentFixture<ConfirmarPago>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmarPago]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmarPago);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
