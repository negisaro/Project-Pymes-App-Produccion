import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddProductoComponent } from './pages/add-producto/add-producto.component';
import { ListProductoComponent } from './pages/list-producto/list-producto.component';
import { ProductoLayoutComponent } from './producto-layout/producto-layout.component';

const routes: Routes = [
  {
    path: '',
    component: ProductoLayoutComponent,
    children: [
      { path: '', redirectTo: 'list-product', pathMatch: 'full' },
      { path: 'list-product', component: ListProductoComponent },
      { path: 'add-product', component: AddProductoComponent }
    ]
  },
  { path: '**', redirectTo: 'list-product' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductoRoutingModule { }
