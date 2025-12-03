import { Routes } from '@angular/router';
import { Home } from './pages/cliente/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Catalogo } from './pages/cliente/catalogo/catalogo';
import { PanelAdmin } from './pages/admin/panel-admin/panel-admin';
import { Pedidos } from './pages/cliente/pedidos/pedidos';
import { CarritoComponent } from './pages/cliente/cart/cart.component';
import { CheckoutComponent } from './pages/cliente/checkout/checkout.component';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  // Catálogo y Carrito
  { path: 'catalogo', component: Catalogo },
  { path: 'carrito', component: CarritoComponent },

  // Panel Admin (Corregido para coincidir con tu login)
  { path: 'panel-admin', component: PanelAdmin },

  // Otras rutas
  { path: 'pedidos', component: Pedidos },
  { path: 'checkout', component: CheckoutComponent },

  // Ruta comodín (por si escriben algo mal, volver al home o login)
  { path: '**', redirectTo: '' },
];
