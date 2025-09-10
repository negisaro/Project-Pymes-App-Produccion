import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IsNotAuthenticatedGuard, IsAuthenticatedGuard } from './auth/guards';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'dashboard',
    loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  // Puedes agregar más rutas aquí, como productos, carrito, perfil, etc.
  { path: 'buscar', loadChildren: () => import('./search/search.module').then(m => m.SearchModule) },
  { path: '**', redirectTo: 'dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
