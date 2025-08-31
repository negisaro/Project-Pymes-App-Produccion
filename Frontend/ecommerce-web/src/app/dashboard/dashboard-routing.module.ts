import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardLayoutComponent } from './layouts/dashboard-layout/dashboard-layout.component';
import { HomePageComponent } from '../shared/pages/home-page/home-page.component';
import { AboutPageComponent } from '../shared/pages/about-page/about-page.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardLayoutComponent, // Solo personal autorizado (admin)
    children: [
      {
        path: 'home',
        component: HomePageComponent,
      },
      {
        path: 'about',
        component: AboutPageComponent,
      },
     /*  {
        path: 'product',
        loadChildren: () =>
          import('../producto/producto.module').then((m) => m.ProductoModule),
        //canActivate: [RoleGuard],
        //data: { roles: [RoleName.ADMIN] }, // Mejor uso del enum
      },
      {
        path: 'user',
        loadChildren: () =>
          import('../user/user.module').then((m) => m.UserModule),
      },
      {
        path: '**',
        redirectTo: 'home',
      }, */
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
