import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  private sidebarOpenSubject = new BehaviorSubject<boolean>(true);
  public sidebarOpen$: Observable<boolean> = this.sidebarOpenSubject.asObservable();

  constructor() {
    // Restaurar estado del localStorage si existe
    const savedState = localStorage.getItem('admin-sidebar-open');
    if (savedState !== null) {
      this.sidebarOpenSubject.next(JSON.parse(savedState));
    }
  }

  /**
   * Obtiene el estado actual del sidebar
   */
  get isOpen(): boolean {
    return this.sidebarOpenSubject.value;
  }

  /**
   * Alterna el estado del sidebar (abierto/cerrado)
   */
  toggle(): void {
    const newState = !this.sidebarOpenSubject.value;
    this.setSidebarState(newState);
  }

  /**
   * Establece el estado del sidebar
   * @param isOpen - true para abrir, false para cerrar
   */
  setSidebarState(isOpen: boolean): void {
    this.sidebarOpenSubject.next(isOpen);
    // Guardar estado en localStorage para persistencia
    localStorage.setItem('admin-sidebar-open', JSON.stringify(isOpen));
  }

  /**
   * Abre el sidebar
   */
  open(): void {
    this.setSidebarState(true);
  }

  /**
   * Cierra el sidebar
   */
  close(): void {
    this.setSidebarState(false);
  }
}
