import { Injectable } from '@angular/core';
import Swal, { SweetAlertIcon, SweetAlertResult } from 'sweetalert2';

/**
 * Servicio para manejo centralizado de notificaciones
 * Wrapper profesional para SweetAlert2 con configuraciones consistentes
 */
@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  // Configuraciones por defecto
  private readonly defaultConfig = {
    toast: {
      toast: true,
      position: 'top-end' as const,
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      didOpen: (toast: HTMLElement) => {
        toast.addEventListener('mouseenter', Swal.stopTimer);
        toast.addEventListener('mouseleave', Swal.resumeTimer);
      }
    },
    confirmation: {
      showCancelButton: true,
      confirmButtonColor: '#28a745',
      cancelButtonColor: '#dc3545',
      confirmButtonText: 'Sí, confirmar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }
  };

  constructor() { }

  // ===== MÉTODOS DE TOAST =====

  /**
   * Muestra un toast de éxito
   */
  success(message: string, title?: string): void {
    Swal.fire({
      ...this.defaultConfig.toast,
      icon: 'success',
      title: title || 'Éxito',
      text: message
    });
  }

  /**
   * Muestra un toast de error
   */
  error(message: string, title?: string): void {
    Swal.fire({
      ...this.defaultConfig.toast,
      icon: 'error',
      title: title || 'Error',
      text: message,
      timer: 5000 // Más tiempo para errores
    });
  }

  /**
   * Muestra un toast de advertencia
   */
  warning(message: string, title?: string): void {
    Swal.fire({
      ...this.defaultConfig.toast,
      icon: 'warning',
      title: title || 'Advertencia',
      text: message,
      timer: 4000
    });
  }

  /**
   * Muestra un toast informativo
   */
  info(message: string, title?: string): void {
    Swal.fire({
      ...this.defaultConfig.toast,
      icon: 'info',
      title: title || 'Información',
      text: message
    });
  }

  // ===== MÉTODOS DE CONFIRMACIÓN =====

  /**
   * Confirmación simple
   */
  async confirm(
    title: string,
    message: string,
    confirmText?: string,
    cancelText?: string
  ): Promise<boolean> {
    const result = await Swal.fire({
      ...this.defaultConfig.confirmation,
      title,
      text: message,
      icon: 'question',
      confirmButtonText: confirmText || this.defaultConfig.confirmation.confirmButtonText,
      cancelButtonText: cancelText || this.defaultConfig.confirmation.cancelButtonText
    });

    return result.isConfirmed;
  }

  /**
   * Confirmación de eliminación
   */
  async confirmDelete(
    itemName?: string,
    customMessage?: string
  ): Promise<boolean> {
    const message = customMessage ||
      `¿Está seguro de eliminar ${itemName ? `"${itemName}"` : 'este elemento'}?`;

    const result = await Swal.fire({
      ...this.defaultConfig.confirmation,
      title: '¿Confirmar eliminación?',
      text: message,
      icon: 'warning',
      confirmButtonText: 'Sí, eliminar',
      confirmButtonColor: '#dc3545',
      footer: 'Esta acción no se puede deshacer'
    });

    return result.isConfirmed;
  }

  /**
   * Confirmación con input de texto
   */
  async confirmWithInput(
    title: string,
    message: string,
    inputPlaceholder: string,
    expectedValue?: string
  ): Promise<{ confirmed: boolean; value?: string }> {
    const result = await Swal.fire({
      title,
      text: message,
      input: 'text',
      inputPlaceholder,
      showCancelButton: true,
      confirmButtonText: 'Confirmar',
      cancelButtonText: 'Cancelar',
      inputValidator: (value) => {
        if (!value) {
          return 'Debe ingresar un valor';
        }
        if (expectedValue && value !== expectedValue) {
          return `Debe ingresar exactamente: ${expectedValue}`;
        }
        return null;
      }
    });

    return {
      confirmed: result.isConfirmed,
      value: result.value
    };
  }

  // ===== MÉTODOS DE LOADING =====

  /**
   * Muestra un modal de loading
   */
  showLoading(message: string = 'Cargando...'): void {
    Swal.fire({
      title: message,
      allowOutsideClick: false,
      allowEscapeKey: false,
      allowEnterKey: false,
      showConfirmButton: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
  }

  /**
   * Cierra el modal de loading
   */
  hideLoading(): void {
    Swal.close();
  }

  /**
   * Wrapper para operaciones con loading automático
   */
  async withLoading<T>(
    operation: () => Promise<T>,
    loadingMessage: string = 'Procesando...'
  ): Promise<T> {
    this.showLoading(loadingMessage);

    try {
      const result = await operation();
      this.hideLoading();
      return result;
    } catch (error) {
      this.hideLoading();
      throw error;
    }
  }

  // ===== MÉTODOS ESPECIALIZADOS =====

  /**
   * Notificación de guardado exitoso
   */
  saveSuccess(itemName?: string): void {
    this.success(
      `${itemName || 'El elemento'} se guardó exitosamente`,
      'Guardado'
    );
  }

  /**
   * Notificación de eliminación exitosa
   */
  deleteSuccess(itemName?: string): void {
    this.success(
      `${itemName || 'El elemento'} se eliminó exitosamente`,
      'Eliminado'
    );
  }

  /**
   * Notificación de actualización exitosa
   */
  updateSuccess(itemName?: string): void {
    this.success(
      `${itemName || 'El elemento'} se actualizó exitosamente`,
      'Actualizado'
    );
  }

  /**
   * Notificación de error de conexión
   */
  connectionError(): void {
    this.error(
      'No se pudo conectar con el servidor. Verifique su conexión a internet.',
      'Error de Conexión'
    );
  }

  /**
   * Notificación de acceso denegado
   */
  accessDenied(): void {
    this.warning(
      'No tiene permisos para realizar esta acción',
      'Acceso Denegado'
    );
  }

  /**
   * Notificación de validación de formulario
   */
  formValidationError(): void {
    this.warning(
      'Por favor, corrija los errores en el formulario antes de continuar',
      'Formulario Inválido'
    );
  }

  // ===== MÉTODOS AVANZADOS =====

  /**
   * Modal personalizado con HTML
   */
  async showCustomModal(config: {
    title: string;
    html: string;
    icon?: SweetAlertIcon;
    showConfirmButton?: boolean;
    showCancelButton?: boolean;
    confirmButtonText?: string;
    cancelButtonText?: string;
    width?: string;
    customClass?: any;
  }): Promise<SweetAlertResult> {
    return await Swal.fire({
      title: config.title,
      html: config.html,
      icon: config.icon,
      showConfirmButton: config.showConfirmButton ?? true,
      showCancelButton: config.showCancelButton ?? false,
      confirmButtonText: config.confirmButtonText || 'Aceptar',
      cancelButtonText: config.cancelButtonText || 'Cancelar',
      width: config.width,
      customClass: config.customClass
    });
  }

  /**
   * Progress bar personalizada
   */
  showProgress(title: string, initialProgress: number = 0): void {
    Swal.fire({
      title,
      html: `
        <div class="progress mb-3" style="height: 20px;">
          <div class="progress-bar" role="progressbar"
               style="width: ${initialProgress}%"
               aria-valuenow="${initialProgress}"
               aria-valuemin="0"
               aria-valuemax="100">
            ${initialProgress}%
          </div>
        </div>
      `,
      showConfirmButton: false,
      allowOutsideClick: false,
      allowEscapeKey: false
    });
  }

  /**
   * Actualiza el progress bar
   */
  updateProgress(progress: number): void {
    const progressBar = document.querySelector('.progress-bar') as HTMLElement;
    if (progressBar) {
      progressBar.style.width = `${progress}%`;
      progressBar.setAttribute('aria-valuenow', progress.toString());
      progressBar.textContent = `${progress}%`;
    }
  }

  /**
   * Muestra múltiples errores de validación
   */
  showValidationErrors(errors: Array<{field: string, message: string}>): void {
    const errorList = errors
      .map(error => `<li><strong>${error.field}:</strong> ${error.message}</li>`)
      .join('');

    this.showCustomModal({
      title: 'Errores de Validación',
      html: `
        <div class="text-start">
          <p>Se encontraron los siguientes errores:</p>
          <ul class="list-unstyled">
            ${errorList}
          </ul>
        </div>
      `,
      icon: 'error',
      width: '600px'
    });
  }
}
