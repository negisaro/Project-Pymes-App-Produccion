import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

@Component({
  selector: 'app-navbar-user-menu',
  template: `
    <div class="user-menu" (keydown.escape)="open.set(false)">
      <button type="button" class="user-menu__btn" (click)="toggle()" aria-haspopup="menu" [attr.aria-expanded]="open()">👤</button>
      <ul *ngIf="open()" class="user-menu__panel" role="menu">
        <li role="none"><a role="menuitem" routerLink="/auth/login" (click)="open.set(false)">Iniciar sesión</a></li>
        <li role="none"><a role="menuitem" routerLink="/auth/register" (click)="open.set(false)">Crear cuenta</a></li>
      </ul>
    </div>
  `,
  styles: [`.user-menu{position:relative;}
  .user-menu__btn{background:none;border:0;cursor:pointer;font-size:1.2rem;}
  .user-menu__panel{list-style:none;margin:0;padding:var(--space-2) 0;position:absolute;right:0;top:calc(100% + 4px);background:var(--color-bg-elevated);border:1px solid var(--color-border);border-radius:var(--radius-md);min-width:160px;box-shadow:var(--shadow-lg);}
  .user-menu__panel a{display:block;padding:var(--space-2) var(--space-4);text-decoration:none;color:var(--color-text);font-size:.85rem;}
  .user-menu__panel a:hover{background:var(--color-bg-subtle);} `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarUserMenuComponent {
  open = signal(false);
  toggle(){ this.open.update(v => !v); }
}
