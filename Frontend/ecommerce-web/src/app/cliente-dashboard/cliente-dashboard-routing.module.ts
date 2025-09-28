import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClienteDashboardComponent } from './pages/cliente-dashboard.component';
import { ClienteLayoutComponent } from './layouts/cliente-layout/cliente-layout.component';

const routes: Routes = [
  {
    path: '',
    component: ClienteLayoutComponent,
    children: [
      { path: '', component: ClienteDashboardComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClienteDashboardRoutingModule {}
