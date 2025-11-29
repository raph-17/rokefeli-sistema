import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Catalogo } from './pages/catalogo/catalogo';
import { ConfirmarPagoComponent } from './pages/confirmar-pago/confirmar-pago';
import { PanelAdmin } from './pages/panel-admin/panel-admin';
import { Pedidos } from './pages/pedidos/pedidos';
import { Users } from './pages/users/users';
// Importa el Carrito
import { CartComponent } from './pages/cart/cart.component'; 

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    
    // Catálogo y Carrito
    { path: 'catalogo', component: Catalogo },
    { path: 'carrito', component: CartComponent }, // <--- ¡AGREGADO!
    { path: 'confirmar-pago', component: ConfirmarPagoComponent }, // Sugiero usar guion

    // Panel Admin (Corregido para coincidir con tu login)
    { path: 'panel-admin', component: PanelAdmin }, 
    
    // Otras rutas admin
    { path: 'pedidos', component: Pedidos },
    { path: 'usuarios', component: Users },
    
    // Ruta comodín (por si escriben algo mal, volver al home o login)
    { path: '**', redirectTo: '' } 
];