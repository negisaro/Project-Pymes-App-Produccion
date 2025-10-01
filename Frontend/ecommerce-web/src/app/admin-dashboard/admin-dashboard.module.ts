import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminDashboardRoutingModule } from './admin-dashboard.routing.module';
import { NgApexchartsModule } from 'ng-apexcharts';

import { AdminDashboardHomeComponent } from './pages/admin-dashboard-home/admin-dashboard-home.component';
import { SharedModule } from '../shared/shared.module';
import { KpiCardsComponent } from './components/kpi-card/kpi-cards.component';
import { VentasChartComponent } from './components/ventas-chart/ventas-chart.component';
import { CategoriasPieChartComponent } from './components/categorias-footer/categorias-pie-chart.component';


@NgModule({
  declarations: [
  AdminDashboardHomeComponent,
  CategoriasPieChartComponent,
  KpiCardsComponent,
  VentasChartComponent,
  ],
  imports: [
    CommonModule,
    AdminDashboardRoutingModule,
    SharedModule,
    NgApexchartsModule
  ],
  exports: [],
})
export class AdminDashboardModule {}
