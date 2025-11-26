import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalCrearProducto } from './modal-crear-producto';

describe('ModalCrearProducto', () => {
  let component: ModalCrearProducto;
  let fixture: ComponentFixture<ModalCrearProducto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalCrearProducto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalCrearProducto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
