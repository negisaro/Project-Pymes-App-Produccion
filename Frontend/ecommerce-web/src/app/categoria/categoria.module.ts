import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CategoriaRoutingModule } from './categoria-routing.module';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { CategoriaLayoutComponent } from './categoria-layout/categoria-layout.component';
import { ListCategoriaComponent } from './pages/list-categoria/list-categoria.component';
import { AddCategoriaComponent } from './pages/add-categoria/add-categoria.component';


@NgModule({
  declarations: [
    CategoriaLayoutComponent,
    ListCategoriaComponent,
    AddCategoriaComponent
  ],
  imports: [
    CommonModule,
    CategoriaRoutingModule,
    ReactiveFormsModule,
    SharedModule
  ]
})
export class CategoriaModule { }
