import { ChangeDetectionStrategy, Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-navbar-search',
  template: `
    <form class="search" role="search" (submit)="onSubmit($event)">
      <input type="text" name="q" placeholder="Buscar productos" aria-label="Buscar" [(ngModel)]="query">
      <button type="submit" aria-label="Buscar">🔍</button>
    </form>
  `,
  styles: [`.search{display:flex;align-items:center;gap:.25rem;background:var(--color-bg-subtle);padding:.35rem .5rem;border-radius:var(--radius-md);}
  .search input{flex:1;border:0;background:transparent;outline:none;}
  .search button{background:none;border:0;cursor:pointer;font-size:1rem;}`],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarSearchComponent {
  query = '';
  @Output() search = new EventEmitter<string>();
  onSubmit(e: Event){
    e.preventDefault();
    this.search.emit(this.query.trim());
  }
}
