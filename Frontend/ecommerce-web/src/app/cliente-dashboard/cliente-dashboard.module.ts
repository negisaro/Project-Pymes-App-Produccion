import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteDashboardComponent } from './pages/cliente-dashboard.component';
import { ClienteDashboardRoutingModule } from './cliente-dashboard-routing.module';
import { FormsModule } from '@angular/forms';
import { ClienteLayoutComponent } from './layouts/cliente-layout/cliente-layout.component';
import { ClienteSidebarComponent } from './components/cliente-sidebar/cliente-sidebar.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    ClienteDashboardComponent,
    ClienteLayoutComponent,
    ClienteSidebarComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ClienteDashboardRoutingModule,
    SharedModule,
  ],
  exports: [
    ClienteDashboardComponent,
    ClienteLayoutComponent,
    ClienteSidebarComponent
  ],
})

export class ClienteDashboardModule {}
