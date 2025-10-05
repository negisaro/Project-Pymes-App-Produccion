import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SuppliersRoutingModule } from './suppliers-routing.module';
import { SharedModule } from '../../shared/shared.module';
import { SuppliersListComponent } from './pages/suppliers-list/suppliers-list.component';
import { SupplierFormComponent } from './pages/supplier-form/supplier-form.component';

@NgModule({
  declarations: [
    SuppliersListComponent,
    SupplierFormComponent,
  ],
  imports: [
    CommonModule,
    SuppliersRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule,
  ],
})
export class SuppliersModule {}