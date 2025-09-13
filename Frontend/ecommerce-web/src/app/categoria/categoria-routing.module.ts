import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoriaLayoutComponent } from './categoria-layout/categoria-layout.component';
import { AddCategoriaComponent } from './pages/add-categoria/add-categoria.component';
import { ListCategoriaComponent } from './pages/list-categoria/list-categoria.component';

const routes: Routes = [
  {
    path: '',
    component: CategoriaLayoutComponent,
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: ListCategoriaComponent },
      { path: 'add', component: AddCategoriaComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoriaRoutingModule { }
