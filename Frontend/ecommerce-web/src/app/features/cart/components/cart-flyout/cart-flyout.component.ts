import { Component, ChangeDetectionStrategy, ElementRef, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CartStore } from '../../../../state/cart.store';

@Component({
  selector: 'app-cart-flyout',
  templateUrl: './cart-flyout.component.html',
  styleUrls: ['./cart-flyout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartFlyoutComponent implements OnInit, OnDestroy {
  @Input() open = false;
  @Output() openChange = new EventEmitter<boolean>();
  private lastFocused: HTMLElement | null = null;

  constructor(public cart: CartStore, private el: ElementRef<HTMLElement>) {}

  ngOnInit(): void {
    if (this.open) this.afterOpen();
  }

  ngOnDestroy(): void {
    if (this.open) this.restoreFocus();
  }

  @HostListener('document:keydown.escape') onEsc(){ if(this.open){ this.close(); } }

  afterOpen(){
    this.lastFocused = document.activeElement as HTMLElement;
    queueMicrotask(() => this.focusFirst());
    document.body.style.overflow = 'hidden';
  }

  focusFirst(){
    const btn = this.el.nativeElement.querySelector('button, a, input, select, textarea') as HTMLElement | null;
    btn?.focus();
  }

  restoreFocus(){
    document.body.style.overflow = '';
    this.lastFocused?.focus();
  }

  close(){
    this.open = false;
    this.openChange.emit(false);
    this.restoreFocus();
  }

  remove(id: string){ this.cart.remove(id); }
  clear(){ this.cart.clear(); }
  update(id: string, qty: number){ this.cart.updateQty(id, qty); }
}
