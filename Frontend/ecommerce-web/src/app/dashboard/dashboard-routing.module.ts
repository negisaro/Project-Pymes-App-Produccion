import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';


const routes: Routes = [
  {
    path: '',
    component: AdminDashboardComponent, // Nuevo layout profesional
    children: [
      // Aquí van solo rutas administrativas protegidas
      {
        path: 'product',
        loadChildren: () =>
          import('../producto/producto.module').then((m) => m.ProductoModule),
      },
      {
        path: 'categoria',
        loadChildren: () =>
          import('../categoria/categoria.module').then(
            (m) => m.CategoriaModule
          ),
      },
      {
        path: 'proveedor',
        loadChildren: () =>
          import('../proveedor/proveedor.module').then(
            (m) => m.ProveedorModule
          ),
      },
      {
        path: 'user',
        loadChildren: () =>
          import('../user/user.module').then((m) => m.UserModule),
      },
      {
        path: '**',
        redirectTo: 'product',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
