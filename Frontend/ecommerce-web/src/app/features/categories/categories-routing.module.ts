import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryListComponent, CategoryFormComponent } from './presentation/pages';

/**
 * Categories Feature Routing Module
 * Defines routes for category management with admin authentication
 */
const routes: Routes = [
  {
    path: '',
    redirectTo: 'list',
    pathMatch: 'full'
  },
  {
    path: 'list',
    component: CategoryListComponent,
    data: {
      title: 'Gestión de Categorías',
      breadcrumb: 'Categorías'
    }
  },
  {
    path: 'create',
    component: CategoryFormComponent,
    data: {
      title: 'Nueva Categoría',
      breadcrumb: 'Nueva Categoría'
    }
  },
  {
    path: 'edit/:id',
    component: CategoryFormComponent,
    data: {
      title: 'Editar Categoría',
      breadcrumb: 'Editar Categoría'
    }
  },
  {
    path: 'view/:id',
    component: CategoryFormComponent,
    data: {
      title: 'Ver Categoría',
      breadcrumb: 'Ver Categoría',
      readonly: true
    }
  },
  {
    path: 'import',
    component: CategoryListComponent, // Temporal - puede ser un componente específico
    data: {
      title: 'Importar Categorías',
      breadcrumb: 'Importar Categorías'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoriesRoutingModule { }
