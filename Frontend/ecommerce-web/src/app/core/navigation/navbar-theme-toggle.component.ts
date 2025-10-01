import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

@Component({
  selector: 'app-navbar-theme-toggle',
  template: `
    <button type="button" class="theme-toggle" (click)="toggle()" [attr.aria-label]="label()">{{ icon() }}</button>
  `,
  styles: [`.theme-toggle{background:none;border:0;cursor:pointer;font-size:1.2rem;line-height:1;}`],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarThemeToggleComponent {
  private prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
  mode = signal<string>(localStorage.getItem('theme') || (this.prefersDark.matches ? 'dark' : 'light'));

  icon() { return this.mode() === 'dark' ? '🌙' : '☀️'; }
  label() { return this.mode() === 'dark' ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro'; }

  constructor(){
    this.apply();
  }

  toggle(){
    this.mode.update(m => m === 'dark' ? 'light' : 'dark');
    localStorage.setItem('theme', this.mode());
    this.apply();
  }

  private apply(){
    const root = document.documentElement;
    if(this.mode() === 'dark') root.classList.add('theme-dark'); else root.classList.remove('theme-dark');
  }
}
