import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { ListUserComponent } from './pages/list-user/list-user.component';
import { UserManagementRoutingModule } from './user-management-routing.module';

@NgModule({
  declarations: [AddUserComponent, ListUserComponent],
  imports: [CommonModule, SharedModule, UserManagementRoutingModule, ReactiveFormsModule],
})
export class UserManagementModule {}
