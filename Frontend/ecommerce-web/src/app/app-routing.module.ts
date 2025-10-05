import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  IsNotAuthenticatedGuard,
  IsAuthenticatedGuard,
  RoleGuard,
} from './core/guards';

import { HomePageComponent } from './shared/pages/home-page/home-page.component';
import { AboutPageComponent } from './shared/pages/about-page/about-page.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { ClientLayoutComponent } from './layouts/client-layout/cliente-layout.component';

import { PreloadSelectedModulesStrategy } from './core/routing/preload-selected-modules.strategy';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', component: HomePageComponent },
      { path: 'home', component: HomePageComponent },
      { path: 'about', component: AboutPageComponent },
      {
        path: 'buscar',
        loadChildren: () => import('./features/search/search.module').then(m => m.SearchModule),
        data: { preload: true }
      },
      // Más rutas públicas aquí (ej: categorías, landing)
    ]
  },
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      {
        path: '',
        canActivate: [IsNotAuthenticatedGuard],
        loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule),
        data: { preload: true }
      }
    ]
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard-admin' },
      {
        path: 'dashboard-admin',
        canActivate: [IsAuthenticatedGuard, RoleGuard],
        data: { roles: ['ROLE_ADMIN'] },
        loadChildren: () => import('./features/admin-dashboard/admin-dashboard.module').then(m => m.AdminDashboardModule)
      }
    ]
  },
  // Nuevo segmento dedicado para cliente (antes estaba en /admin/dashboard-cliente)
  {
    path: 'cliente',
    component: ClientLayoutComponent,
    canActivate: [IsAuthenticatedGuard, RoleGuard],
    // Ampliamos espectro de roles equivalentes: ROLE_CLIENT / CLIENT / CLIENTE / ROLE_CLIENTE / USER
    data: { roles: ['ROLE_CLIENT','CLIENT','CLIENTE','ROLE_CLIENTE','ROLE_USER','USER'] },
    children: [
      {
        path: '',
        loadChildren: () => import('./features/client-dashboard/cliente-dashboard.module').then(m => m.ClienteDashboardModule)
      }
    ]
  },
  // Redirección legacy para mantener compatibilidad con enlaces antiguos
  { path: 'admin/dashboard-cliente', redirectTo: '/cliente', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadSelectedModulesStrategy })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
