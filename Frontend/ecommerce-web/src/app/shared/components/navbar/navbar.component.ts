import { Component, OnInit, inject, HostListener, ElementRef, computed } from '@angular/core';
import { CartFacade } from '../../../features/cart/presentation/facades/cart.facade';
import { BehaviorSubject } from 'rxjs';
import { toObservable } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../features/auth/services/auth.service';

@Component({
  selector: 'shared-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  showProfileMenu = false;
  cartCount = 0;
  private cartFacade = inject(CartFacade);
  favoritesCount = 0;
  private authService = inject(AuthService);
  private router = inject(Router);
  notificationsCount = 0;
  showSearch = false;

  // Usar computed signals para obtener el usuario reactivamente
  public currentUser = computed(() => this.authService.currentUser());
  public isAuthenticated = computed(() => this.authService.isAuthenticated());

  // Computed properties para roles
  public isAdmin = computed(() => {
    const user = this.currentUser();
    return !!user && Array.isArray(user.roles) &&
           user.roles.some((r: any) => r.name === 'ROLE_ADMIN');
  });

  public isCliente = computed(() => {
    const user = this.currentUser();
    return !!user && Array.isArray(user.roles) &&
           user.roles.some((r: any) => r.name === 'ROLE_CLIENT');
  });

  public isSupervisor = computed(() => {
    const user = this.currentUser();
    return !!user && Array.isArray(user.roles) &&
           user.roles.some((r: any) => r.name === 'ROLE_SUPERVISOR');
  });

  public isEmpleado = computed(() => {
    const user = this.currentUser();
    return !!user && Array.isArray(user.roles) &&
           user.roles.some((r: any) => r.name === 'ROLE_EMPLEADO');
  });

  constructor(private eRef: ElementRef) {}

  ngOnInit() {
    // Suscribirse al contador del carrito
    this.cartFacade.itemCount$.subscribe((count: number) => {
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

  onSearch(term: string) {
    if (term && term.trim().length > 0) {
      // Redirige a la página de búsqueda con el término
      this.router.navigate(['/buscar'], { queryParams: { q: term } });
      this.showSearch = false;
    }
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
      this.router.navigate(['/']);
    }
  }
}
