import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  IsNotAuthenticatedGuard,
  IsAuthenticatedGuard,
  RoleGuard,
} from './auth/guards';

import { HomePageComponent } from './shared/pages/home-page/home-page.component';
import { AboutPageComponent } from './shared/pages/about-page/about-page.component';
// import { ContactPageComponent } from './shared/pages/contact-page/contact-page.component'; // Descomenta si existe

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'home', component: HomePageComponent },
  // Auth
  {
    path: 'auth',
    loadChildren: () =>
      import('./auth/auth.module').then((m) => m.AuthModule),
  },
  // Dashboard Admin
  {
    path: 'dashboard-admin',
    canActivate: [IsAuthenticatedGuard, RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    loadChildren: () =>
      import('./admin-dashboard/admin-dashboard.module').then((m) => m.AdminDashboardModule),
  },
  // Dashboard Cliente
  {
    path: 'dashboard-cliente',
    canActivate: [IsAuthenticatedGuard, RoleGuard],
    data: { roles: ['ROLE_CLIENT'] },
    loadChildren: () =>
      import('./user/user.module').then((m) => m.UserModule),
  },
  // Buscar
  {
    path: 'buscar',
    loadChildren: () =>
      import('./search/search.module').then((m) => m.SearchModule),
  },
  // About
  { path: 'about', component: AboutPageComponent },
  // Contacto (descomenta si tienes el componente)
  // { path: 'contact', component: ContactPageComponent },
  // Wildcard
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
