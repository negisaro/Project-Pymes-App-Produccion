import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SuppliersListComponent } from './pages/suppliers-list/suppliers-list.component';
import { SupplierFormComponent } from './pages/supplier-form/supplier-form.component';

const routes: Routes = [
  { path: '', component: SuppliersListComponent },
  { path: 'add', component: SupplierFormComponent },
  { path: 'edit/:id', component: SupplierFormComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SuppliersRoutingModule {}