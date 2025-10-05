import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminDashboardHomeComponent } from './pages/admin-dashboard-home/admin-dashboard-home.component';


const routes: Routes = [
  { path: '', component: AdminDashboardHomeComponent },
  { path: 'home', component: AdminDashboardHomeComponent },
  { path: 'product', loadChildren: () => import('../products/products.module').then(m => m.ProductsModule) },
  { path: 'categoria', loadChildren: () => import('../categories/categories.module').then(m => m.CategoriesModule) },
  { path: 'suppliers', loadChildren: () => import('../suppliers/suppliers.module').then(m => m.SuppliersModule) },
  { path: 'user', loadChildren: () => import('../user-management/user-management.module').then(m => m.UserManagementModule) },
  { path: 'orders', loadChildren: () => import('../orders/orders.module').then(m => m.OrdersModule) },
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminDashboardRoutingModule {}
