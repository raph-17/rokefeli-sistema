import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Reservas } from './pages/reservas/reservas';
import { Paquetes } from './pages/paquetes/paquetes';
import { ConfirmarPagoComponent } from './pages/confirmar-pago/confirmar-pago';
import { PanelAdmin } from './pages/panel-admin/panel-admin';
import { Pedidos } from './pages/pedidos/pedidos';
import { Users } from './pages/users/users';
import { ModalCrearProductoComponent } from './components/modal-crear-producto/modal-crear-producto';

export const routes: Routes = [
    {path:'', component: Home},
    {path:'login', component: Login},
    {path:'register', component: Register},
    {path:'reservas', component: Reservas},
    {path:'paquetes', component: Paquetes},
    {path:'confirmarPago', component: ConfirmarPagoComponent},
    {path:'panelAdmin', component: PanelAdmin},
    {path:'pedidos', component: Pedidos},
    {path:'usuarios', component: Users},
    {path:'modalCrearProducto', component: ModalCrearProductoComponent}
];
