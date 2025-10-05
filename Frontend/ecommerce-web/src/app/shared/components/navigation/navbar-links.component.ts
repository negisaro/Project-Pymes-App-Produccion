import { ChangeDetectionStrategy, Component, EventEmitter, Output } from '@angular/core';

interface NavLink { label: string; path: string; exact?: boolean; }

@Component({
  selector: 'app-navbar-links',
  template: `
    <ul class="nav-links" role="menubar">
      <li *ngFor="let l of links" role="none">
  <a role="menuitem" [routerLink]="l.path" routerLinkActive="is-active" [routerLinkActiveOptions]="{exact: !!l.exact}" (click)="navigate.emit()">{{ l.label }}</a>
      </li>
    </ul>
  `,
  styles: [`.nav-links{list-style:none;display:flex;gap:var(--space-5);margin:0;padding:0;}
  .nav-links a{text-decoration:none;color:var(--color-text);font-weight:500;}
  .nav-links a.is-active{color:var(--color-primary);} `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarLinksComponent {
  @Output() navigate = new EventEmitter<void>();
  links: NavLink[] = [
    { label: 'Inicio', path: '/home', exact: true },
    { label: 'Catálogo', path: '/catalogo' },
    { label: 'Ofertas', path: '/ofertas' },
    { label: 'Contacto', path: '/contacto' }
  ];
}
