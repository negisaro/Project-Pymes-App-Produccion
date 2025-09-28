import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-layout',
  template: `
    <div class="admin-layout">
  <app-admin-sidebar></app-admin-sidebar>
      <div class="admin-content">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent {}
