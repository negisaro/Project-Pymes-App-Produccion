import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { LayoutUserComponent } from './layouts/layout-user/layout-user.component';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { ListUserComponent } from './pages/list-user/list-user.component';
import { UserRoutingModule } from './user-routing.module';



@NgModule({
  declarations: [
    AddUserComponent,
    LayoutUserComponent,
    ListUserComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    UserRoutingModule,
    ReactiveFormsModule  
  ]
})
export class UserModule { }
