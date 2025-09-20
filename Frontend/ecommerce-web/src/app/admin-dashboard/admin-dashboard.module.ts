import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard.component';

@NgModule({
  declarations: [AdminDashboardComponent],
  imports: [CommonModule, RouterModule],
  exports: [AdminDashboardComponent]
})
export class AdminDashboardModule {}
