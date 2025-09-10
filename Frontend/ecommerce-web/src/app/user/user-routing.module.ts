import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleName } from './interfaces/user.interface';
import { LayoutUserComponent } from './layouts/layout-user/layout-user.component';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { ListUserComponent } from './pages/list-user/list-user.component';
import { RoleGuard } from '../auth/guards/role.guard';

const routes: Routes = [
   {
    path: '',
    component: LayoutUserComponent,
  },
  {
    path: 'list-user',
    component: ListUserComponent
  },
  {
    path: 'add-user',
    component: AddUserComponent,
    canActivate: [RoleGuard],
    data: { roles: [RoleName.ADMIN] }
  },
  {
    path: 'edit-user/:id',
    component: AddUserComponent,
    canActivate: [RoleGuard],
    data: { roles: [RoleName.ADMIN] }
  },
  {
    path: 'page-user/:page',
    component: ListUserComponent
  },
  {
    path: '**',
    redirectTo: 'home',
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
