import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Componentes del sistema
import { HeroSectionComponent } from './hero-section/hero-section.component';
import { DataTableComponent } from './data-table/data-table.component';
import { PaginationComponent } from './pagination/pagination.component';
import { FiltersPanelComponent } from './filters-panel/filters-panel.component';
import { MultiSelectComponent } from './multi-select/multi-select.component';

// Servicios
import { ApiResponseHandlerService } from '../../services/api-response-handler.service';
import { LoadingManagerService } from '../../services/loading-manager.service';
import { NotificationService } from '../../services/notification.service';
import { ExportService } from '../../services/export.service';

/**
 * Módulo de gestión de datos empresarial
 * Proporciona componentes y servicios reutilizables para operaciones CRUD
 */
@NgModule({
  declarations: [
    // Componentes principales
    HeroSectionComponent,
    DataTableComponent,
    PaginationComponent,
    FiltersPanelComponent,
    MultiSelectComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  providers: [
    // Servicios especializados
    ApiResponseHandlerService,
    LoadingManagerService,
    NotificationService,
    ExportService
  ],
  exports: [
    // Exportar todos los componentes para uso externo
    HeroSectionComponent,
    DataTableComponent,
    PaginationComponent,
    FiltersPanelComponent,
    MultiSelectComponent,

    // Exportar módulos comunes para conveniencia
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class DataManagementModule {

  /**
   * Configuración para el módulo raíz de la aplicación
   * Garantiza que los servicios se instancien una sola vez
   */
  static forRoot() {
    return {
      ngModule: DataManagementModule,
      providers: [
        ApiResponseHandlerService,
        LoadingManagerService,
        NotificationService,
        ExportService
      ]
    };
  }

  /**
   * Configuración para módulos feature
   * No incluye providers para evitar múltiples instancias de servicios
   */
  static forFeature() {
    return {
      ngModule: DataManagementModule,
      providers: []
    };
  }
}
