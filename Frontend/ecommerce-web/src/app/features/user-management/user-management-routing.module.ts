import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleName } from '../../core/models/user.model';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { ListUserComponent } from './pages/list-user/list-user.component';
import { RoleGuard } from '../../core/guards/role.guard';

const routes: Routes = [
  { path: '', component: ListUserComponent },
  { path: 'add-user', component: AddUserComponent },
  { path: 'edit-user/:id', component: AddUserComponent },
  { path: 'page-user/:page', component: ListUserComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule { }
