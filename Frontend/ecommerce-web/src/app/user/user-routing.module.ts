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
    children: [
      { path: '', component: ListUserComponent },
      { path: 'add-user', component: AddUserComponent },
      { path: 'edit-user/:id', component: AddUserComponent },
      { path: 'page-user/:page', component: ListUserComponent },
      { path: '**', redirectTo: '' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
