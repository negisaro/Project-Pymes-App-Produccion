import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// Routing
import { CategoriesRoutingModule } from './categories-routing.module';

// Shared modules
import { DataManagementModule } from '../../shared/components/data-management/data-management.module';

// Core services and providers
import {
  CategoryService,
  CategoryRepository,
  CategoryHttpRepository,
  CATEGORY_REPOSITORY_TOKEN
} from './core';

// Presentation components
import {
  CategoryListComponent,
  CategoryFormComponent
} from './presentation/pages';

// Import missing finalize operator
import { finalize } from 'rxjs';

/**
 * Categories Feature Module
 * Self-contained module with Clean Architecture implementation
 * Uses enterprise data-management components
 */
@NgModule({
  declarations: [
    // Pages
    CategoryListComponent,
    CategoryFormComponent

    // Components will be added here when created
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,

    // Routing
    CategoriesRoutingModule,

    // Shared enterprise components
    DataManagementModule.forFeature()
  ],
  providers: [
    // Los servicios ya están proporcionados con providedIn: 'root'
    // Solo mantenemos proveedores específicos si los necesitamos
  ]
})
export class CategoriesModule { }
