import { Injectable } from '@angular/core';
import { Observable, throwError, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ApiResponse, ValidationError } from '../interfaces/api-response.interface';

/**
 * Servicio para manejo centralizado de respuestas de API
 * Proporciona métodos consistentes para procesar respuestas del backend
 */
@Injectable({
  providedIn: 'root'
})
export class ApiResponseHandlerService {

  constructor() { }

  /**
   * Extrae datos de una respuesta API exitosa
   */
  extractData<T>(response: ApiResponse<T>): T {
    if (response && response.success && response.data !== undefined) {
      return response.data;
    }

    throw new Error(response?.message || 'Respuesta inválida del servidor');
  }

  /**
   * Procesa una respuesta Observable de la API
   */
  handleApiResponse<T>(response$: Observable<ApiResponse<T>>): Observable<T> {
    return response$.pipe(
      map(response => this.extractData(response)),
      catchError(error => this.handleError(error))
    );
  }

  /**
   * Manejo centralizado de errores de API
   */
  handleError(error: any): Observable<never> {
    console.error('Error en API:', error);

    let errorMessage = 'Error desconocido en el servidor';
    let validationErrors: ValidationError[] = [];

    // Error de respuesta HTTP
    if (error?.error) {
      const errorResponse = error.error;

      // Respuesta con formato ApiResponse
      if (errorResponse.message) {
        errorMessage = errorResponse.message;
      }

      // Errores de validación
      if (errorResponse.errors && Array.isArray(errorResponse.errors)) {
        validationErrors = errorResponse.errors;
      }

      // Error simple de string
      if (typeof errorResponse === 'string') {
        errorMessage = errorResponse;
      }
    }
    // Error de conexión o timeout
    else if (error?.message) {
      errorMessage = error.message;
    }
    // Error HTTP status específicos
    else if (error?.status) {
      errorMessage = this.getHttpStatusMessage(error.status);
    }

    // Crear error estructurado
    const structuredError = {
      message: errorMessage,
      validationErrors,
      originalError: error,
      timestamp: new Date().toISOString()
    };

    return throwError(() => structuredError);
  }

  /**
   * Mensajes amigables para códigos HTTP
   */
  private getHttpStatusMessage(status: number): string {
    const statusMessages: Record<number, string> = {
      400: 'Solicitud inválida - Verifique los datos enviados',
      401: 'No autorizado - Inicie sesión nuevamente',
      403: 'Acceso denegado - No tiene permisos para esta acción',
      404: 'Recurso no encontrado',
      409: 'Conflicto - El recurso ya existe o está en uso',
      422: 'Datos inválidos - Verifique la información',
      429: 'Demasiadas solicitudes - Intente más tarde',
      500: 'Error interno del servidor',
      502: 'Error de conexión con el servidor',
      503: 'Servicio no disponible temporalmente',
      504: 'Timeout del servidor'
    };

    return statusMessages[status] || `Error HTTP ${status}`;
  }

  /**
   * Valida si una respuesta es exitosa
   */
  isSuccessResponse<T>(response: ApiResponse<T>): boolean {
    return response && response.success === true;
  }

  /**
   * Extrae errores de validación de una respuesta
   */
  extractValidationErrors<T>(response: ApiResponse<T>): ValidationError[] {
    return response?.errors || [];
  }

  /**
   * Crea una respuesta mock para testing
   */
  createMockResponse<T>(data: T, success: boolean = true, message: string = ''): ApiResponse<T> {
    return {
      data,
      success,
      message,
      timestamp: new Date().toISOString()
    };
  }

  /**
   * Crea una respuesta de error mock
   */
  createMockErrorResponse(message: string, errors?: ValidationError[]): ApiResponse<null> {
    return {
      data: null,
      success: false,
      message,
      errors,
      timestamp: new Date().toISOString()
    };
  }

  /**
   * Wrapper para requests que no usan ApiResponse
   */
  wrapLegacyResponse<T>(data: T): Observable<T> {
    return of(data);
  }

  /**
   * Convierte errores de validación a formato de formulario Angular
   */
  convertToFormErrors(validationErrors: ValidationError[]): Record<string, string[]> {
    const formErrors: Record<string, string[]> = {};

    validationErrors.forEach(error => {
      if (!formErrors[error.field]) {
        formErrors[error.field] = [];
      }
      formErrors[error.field].push(error.message);
    });

    return formErrors;
  }

  /**
   * Verifica si hay errores de un campo específico
   */
  hasFieldError(validationErrors: ValidationError[], fieldName: string): boolean {
    return validationErrors.some(error => error.field === fieldName);
  }

  /**
   * Obtiene mensaje de error para un campo específico
   */
  getFieldErrorMessage(validationErrors: ValidationError[], fieldName: string): string {
    const fieldError = validationErrors.find(error => error.field === fieldName);
    return fieldError?.message || '';
  }
}
