import { Component, OnInit, inject, HostListener, ElementRef } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { BehaviorSubject } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'shared-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  showProfileMenu = false;
  cartCount = 0;
  private cartService = inject(CartService);
  favoritesCount = 0;
  private authService = inject(AuthService);
  private router = inject(Router);
  user$ = new BehaviorSubject<any>(null);
  userObservable = this.authService.currentUser$;
  notificationsCount = 0;
  showSearch = false;

  get isAdmin(): boolean {
    const u = this.user$.value;
    return !!u && Array.isArray(u.roles) && u.roles.some((r: any) => r.name === 'ROLE_ADMIN');
  }

  get isCliente(): boolean {
    const u = this.user$.value;
    return !!u && Array.isArray(u.roles) && u.roles.some((r: any) => r.name === 'ROLE_CLIENT');
  }

  constructor(private eRef: ElementRef) {}

  ngOnInit() {
    // Forzar estado inicial a null por seguridad
    this.user$.next(null);
    this.userObservable.subscribe((user: any) => {
      this.user$.next(user ?? null);
    });
    // Suscribirse al contador del carrito
    this.cartService.cartCount$.subscribe(count => {
      this.cartCount = count;
    });
  }

  toggleProfileMenu(event: Event) {
    event.preventDefault();
    this.showProfileMenu = !this.showProfileMenu;
  }

  closeProfileMenu() {
    this.showProfileMenu = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (this.showProfileMenu && !this.eRef.nativeElement.contains(event.target)) {
      this.closeProfileMenu();
    }
  }

  getRoleLabel(role?: string): string {
    switch (role) {
      case 'ROLE_ADMIN': return 'Administrador';
      case 'ROLE_USER': return 'Usuario';
      case 'ROLE_CLIENT': return 'Cliente';
      default: return 'Usuario';
    }
  }

  isSupervisor(user?: any): boolean {
    const u = user ?? this.user$.value;
    return !!u?.roles?.some((r: any) => r.name === 'ROLE_SUPERVISOR');
  }

  onSearch(term: string) {
    if (term && term.trim().length > 0) {
      // Redirige a la página de búsqueda con el término
      this.router.navigate(['/buscar'], { queryParams: { q: term } });
      this.showSearch = false;
    }
  }

  isEmpleado(user?: any): boolean {
    const u = user ?? this.user$.value;
    return !!u?.roles?.some((r: any) => r.name === 'ROLE_EMPLEADO');
  }

  isMobile(): boolean {
    return window.innerWidth < 992;
  }

  async logout() {
    const result = await Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#1976d2',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
    });
    if (result.isConfirmed) {
      this.authService.logout();
      this.user$.next(null);
      this.router.navigate(['/']);
    }
  }
}
