import { Component } from '@angular/core';

/**
 * Componente duplicado obsoleto. Mantener no referenciado.
 * Se renombra el selector para evitar colisión con el layout principal en core/layout.
 */
@Component({
  selector: 'app-admin-dashboard-legacy-layout',
  template: '<!-- Obsoleto: usar core/layout/admin-layout/admin-layout.component -->',
  standalone: false,
  styleUrls: ['./admin-layout.component.css']
})
export class AdminDashboardLegacyLayoutComponent {}
