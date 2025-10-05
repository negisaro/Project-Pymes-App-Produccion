import { Component, OnInit, inject, HostListener, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../features/auth/services/auth.service';
import { BehaviorSubject } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-topbar',
  templateUrl: './admin-topbar.component.html',
  styleUrls: ['./admin-topbar.component.scss']
})
export class AdminTopbarComponent implements OnInit {

  private authService = inject(AuthService);
  private router = inject(Router);

  user$ = new BehaviorSubject<any>(null);
  userObservable = this.authService.currentUser$;
  showUserDropdown = false;
  showNotifications = false;
  notificationsCount = 3; // Simulado por ahora
  currentDate = new Date(); // Fecha actual como propiedad

  constructor(private eRef: ElementRef) {}

  ngOnInit() {
    // Suscribirse al usuario actual
    this.userObservable.subscribe((user: any) => {
      this.user$.next(user ?? null);
    });
  }

  // Obtener nombre completo del usuario
  get fullName(): string {
    const user = this.user$.value;
    if (!user) return '';
    return `${user.name || ''} ${user.lastname || ''}`.trim() || user.username || 'Usuario';
  }

  // Obtener rol principal del usuario
  get userRole(): string {
    const user = this.user$.value;
    if (!user || !user.roles || !user.roles.length) return '';
    return user.roles[0]?.name?.replace('ROLE_', '') || '';
  }

  // Obtener color del badge según el rol
  get roleColor(): string {
    const role = this.userRole.toLowerCase();
    switch (role) {
      case 'admin': return 'bg-danger';
      case 'user': return 'bg-primary';
      case 'client': return 'bg-success';
      default: return 'bg-secondary';
    }
  }

  // Toggle dropdown de usuario
  toggleUserDropdown(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.showUserDropdown = !this.showUserDropdown;
    this.showNotifications = false; // Cerrar notificaciones si están abiertas
  }

  // Toggle notificaciones
  toggleNotifications(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.showNotifications = !this.showNotifications;
    this.showUserDropdown = false; // Cerrar dropdown de usuario si está abierto
  }

  // Cerrar dropdowns
  closeDropdowns() {
    this.showUserDropdown = false;
    this.showNotifications = false;
  }

  // Escuchar clicks fuera del componente para cerrar dropdowns
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    if (!this.eRef.nativeElement.contains(event.target)) {
      this.closeDropdowns();
    }
  }

  // Logout funcional con confirmación
  async logout() {
    const result = await Swal.fire({
      title: '¿Cerrar sesión?',
      text: 'Se cerrará la sesión actual del panel administrativo',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      background: '#fff',
      customClass: {
        popup: 'shadow-lg border-0 rounded-3',
        title: 'fw-bold text-dark',
        confirmButton: 'btn-lg px-4',
        cancelButton: 'btn-lg px-4'
      }
    });

    if (result.isConfirmed) {
      this.authService.logout();

      await Swal.fire({
        title: 'Sesión cerrada',
        text: 'Has salido del panel administrativo exitosamente',
        icon: 'success',
        timer: 2000,
        showConfirmButton: false,
        background: '#fff',
        customClass: {
          popup: 'shadow-lg border-0 rounded-3',
          title: 'fw-bold text-dark'
        }
      });

      this.router.navigate(['/auth/login']);
    }
  }

  // Navegar a perfil (por implementar)
  goToProfile() {
    this.closeDropdowns();
    // this.router.navigate(['/admin/profile']);
    console.log('Ir a perfil - Por implementar');
  }

  // Navegar a configuración (por implementar)
  goToSettings() {
    this.closeDropdowns();
    // this.router.navigate(['/admin/settings']);
    console.log('Ir a configuración - Por implementar');
  }

}
