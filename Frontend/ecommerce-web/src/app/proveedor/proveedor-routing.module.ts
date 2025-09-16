import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddProveedorComponent } from './pages/add-proveedor/add-proveedor.component';
import { ListProveedorComponent } from './pages/list-proveedor/list-proveedor.component';
import { ProveedorLayoutComponent } from './proveedor-layout/proveedor-layout.component';

const routes: Routes = [
  {
    path: '',
    component: ProveedorLayoutComponent,
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: ListProveedorComponent },
      { path: 'add', component: AddProveedorComponent },
      { path: 'edit/:id', component: AddProveedorComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProveedorRoutingModule {}
