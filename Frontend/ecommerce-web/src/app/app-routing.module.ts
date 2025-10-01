import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {
  IsNotAuthenticatedGuard,
  IsAuthenticatedGuard,
  RoleGuard,
} from './auth/guards';

import { HomePageComponent } from './shared/pages/home-page/home-page.component';
import { AboutPageComponent } from './shared/pages/about-page/about-page.component';
import { MainLayoutComponent } from './core/layout/main-layout/main-layout.component';
import { AdminLayoutComponent } from './core/layout/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './core/layout/auth-layout/auth-layout.component';
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
        loadChildren: () => import('./search/search.module').then(m => m.SearchModule),
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
        loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule),
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
        loadChildren: () => import('./admin-dashboard/admin-dashboard.module').then(m => m.AdminDashboardModule)
      },
    ]
  },
  // Nuevo segmento dedicado para cliente (antes estaba en /admin/dashboard-cliente)
  {
    path: 'cliente',
    canActivate: [IsAuthenticatedGuard, RoleGuard],
    // Ampliamos espectro de roles equivalentes: ROLE_CLIENT / CLIENT / CLIENTE / ROLE_CLIENTE / USER
    data: { roles: ['ROLE_CLIENT','CLIENT','CLIENTE','ROLE_CLIENTE','ROLE_USER','USER'] },
    loadChildren: () => import('./cliente-dashboard/cliente-dashboard.module').then(m => m.ClienteDashboardModule)
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
