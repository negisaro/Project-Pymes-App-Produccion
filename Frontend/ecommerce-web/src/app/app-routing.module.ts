import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IsNotAuthenticatedGuard, IsAuthenticatedGuard, RoleGuard } from './auth/guards';

import { HomePageComponent } from './shared/pages/home-page/home-page.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'home', component: HomePageComponent },
  {
    path: 'dashboard',
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  // Puedes agregar más rutas aquí, como productos, carrito, perfil, etc.
  { path: 'buscar', loadChildren: () => import('./search/search.module').then(m => m.SearchModule) },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
