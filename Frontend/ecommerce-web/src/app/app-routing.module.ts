import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IsNotAuthenticatedGuard, IsAuthenticatedGuard } from './auth/guards';

const routes: Routes = [
    {
    path: 'auth',
    canActivate: [IsNotAuthenticatedGuard],
    loadChildren: () => import('./auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'dashboard',
    canActivate: [IsAuthenticatedGuard],
    loadChildren: () =>
      import('./dashboard/dashboard.module').then((m) => m.DashboardModule),
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
