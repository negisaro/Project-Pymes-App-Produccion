import { NgModule } from '@angular/core';
import { CommonModule, DatePipe, CurrencyPipe } from '@angular/common';
import { AdminDashboardRoutingModule } from './admin-dashboard.routing.module';
import { NgApexchartsModule } from 'ng-apexcharts';

import { AdminDashboardHomeComponent } from './pages/admin-dashboard-home/admin-dashboard-home.component';
import { SharedModule } from '../shared/shared.module';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AdminSidebarComponent } from './components/admin-sidebar/admin-sidebar.component';
import { KpiCardsComponent } from './components/kpi-card/kpi-cards.component';
import { VentasChartComponent } from './components/ventas-chart/ventas-chart.component';
import { CategoriasPieChartComponent } from './components/categorias-footer/categorias-pie-chart.component';


@NgModule({
  declarations: [
    AdminDashboardHomeComponent,
    AdminLayoutComponent,
    AdminSidebarComponent,
    CategoriasPieChartComponent,
    KpiCardsComponent,
    VentasChartComponent,

  ],
  imports: [
    CommonModule,
    DatePipe,
    CurrencyPipe,
    AdminDashboardRoutingModule,
    SharedModule,
    NgApexchartsModule
  ],
  exports: [
    AdminLayoutComponent,
    AdminSidebarComponent
  ],
})
export class AdminDashboardModule {}
