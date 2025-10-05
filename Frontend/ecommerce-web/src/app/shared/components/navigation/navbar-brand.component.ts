import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-navbar-brand',
  template: `<a routerLink="/" class="brand" aria-label="Inicio">Pymes<span>App</span></a>`,
  styles: [`.brand { font-weight:600; font-size:1.25rem; text-decoration:none; color:var(--color-text-strong);} .brand span{color:var(--color-primary);}`],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarBrandComponent {}
