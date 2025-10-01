import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminDashboardHomeComponent } from './pages/admin-dashboard-home/admin-dashboard-home.component';


const routes: Routes = [
  { path: '', component: AdminDashboardHomeComponent },
  { path: 'home', component: AdminDashboardHomeComponent },
  { path: 'product', loadChildren: () => import('../producto/producto.module').then(m => m.ProductoModule) },
  { path: 'categoria', loadChildren: () => import('../categoria/categoria.module').then(m => m.CategoriaModule) },
  { path: 'proveedor', loadChildren: () => import('../proveedor/proveedor.module').then(m => m.ProveedorModule) },
  { path: 'user', loadChildren: () => import('../user/user.module').then(m => m.UserModule) },
  { path: 'pedido', loadChildren: () => import('../pedido/pedido.module').then(m => m.PedidoModule) },
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminDashboardRoutingModule {}
