import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * Servicio para gestión centralizada de estados de carga
 * Permite controlar y mostrar indicadores de loading de forma consistente
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingManagerService {

  private loadingStates = new Map<string, BehaviorSubject<boolean>>();
  private globalLoading$ = new BehaviorSubject<boolean>(false);
  private loadingMessages = new Map<string, string>();

  constructor() { }

  /**
   * Inicia estado de carga para un contexto específico
   */
  startLoading(context: string, message?: string): void {
    if (!this.loadingStates.has(context)) {
      this.loadingStates.set(context, new BehaviorSubject<boolean>(false));
    }

    this.loadingStates.get(context)!.next(true);

    if (message) {
      this.loadingMessages.set(context, message);
    }

    this.updateGlobalLoading();
  }

  /**
   * Detiene estado de carga para un contexto específico
   */
  stopLoading(context: string): void {
    if (this.loadingStates.has(context)) {
      this.loadingStates.get(context)!.next(false);
      this.loadingMessages.delete(context);
    }

    this.updateGlobalLoading();
  }

  /**
   * Obtiene el estado de carga para un contexto específico
   */
  isLoading(context: string): Observable<boolean> {
    if (!this.loadingStates.has(context)) {
      this.loadingStates.set(context, new BehaviorSubject<boolean>(false));
    }

    return this.loadingStates.get(context)!.asObservable();
  }

  /**
   * Obtiene el estado de carga global
   */
  isGlobalLoading(): Observable<boolean> {
    return this.globalLoading$.asObservable();
  }

  /**
   * Verifica si hay algún loading activo
   */
  hasAnyLoading(): boolean {
    return Array.from(this.loadingStates.values()).some(state => state.value);
  }

  /**
   * Obtiene mensaje de carga para un contexto
   */
  getLoadingMessage(context: string): string {
    return this.loadingMessages.get(context) || 'Cargando...';
  }

  /**
   * Obtiene todos los contextos con loading activo
   */
  getActiveLoadingContexts(): string[] {
    const activeContexts: string[] = [];

    this.loadingStates.forEach((state, context) => {
      if (state.value) {
        activeContexts.push(context);
      }
    });

    return activeContexts;
  }

  /**
   * Detiene todos los loading activos
   */
  stopAllLoading(): void {
    this.loadingStates.forEach((state, context) => {
      state.next(false);
      this.loadingMessages.delete(context);
    });

    this.updateGlobalLoading();
  }

  /**
   * Wrapper para operaciones asíncronas con loading automático
   */
  withLoading<T>(
    operation: () => Observable<T>,
    context: string,
    message?: string
  ): Observable<T> {
    return new Observable<T>(subscriber => {
      this.startLoading(context, message);

      const subscription = operation().subscribe({
        next: (value) => {
          subscriber.next(value);
        },
        error: (error) => {
          this.stopLoading(context);
          subscriber.error(error);
        },
        complete: () => {
          this.stopLoading(context);
          subscriber.complete();
        }
      });

      // Cleanup en caso de unsubscribe
      return () => {
        this.stopLoading(context);
        subscription.unsubscribe();
      };
    });
  }

  /**
   * Wrapper para promesas con loading automático
   */
  async withLoadingPromise<T>(
    operation: () => Promise<T>,
    context: string,
    message?: string
  ): Promise<T> {
    this.startLoading(context, message);

    try {
      const result = await operation();
      this.stopLoading(context);
      return result;
    } catch (error) {
      this.stopLoading(context);
      throw error;
    }
  }

  /**
   * Crea un decorator para métodos que necesitan loading
   */
  createLoadingDecorator(context: string, message?: string) {
    return (target: any, propertyKey: string, descriptor: PropertyDescriptor) => {
      const originalMethod = descriptor.value;

      descriptor.value = async function (...args: any[]) {
        const loadingManager = (this as any).loadingManager ||
                               (this as any).injector?.get(LoadingManagerService);

        if (!loadingManager) {
          return originalMethod.apply(this, args);
        }

        return loadingManager.withLoadingPromise(
          () => originalMethod.apply(this, args),
          context,
          message
        );
      };

      return descriptor;
    };
  }

  /**
   * Actualiza el estado de loading global
   */
  private updateGlobalLoading(): void {
    const hasAnyLoading = this.hasAnyLoading();
    this.globalLoading$.next(hasAnyLoading);
  }

  /**
   * Limpia contextos inactivos para optimizar memoria
   */
  cleanup(): void {
    const contextsToRemove: string[] = [];

    this.loadingStates.forEach((state, context) => {
      if (!state.observed && !state.value) {
        contextsToRemove.push(context);
      }
    });

    contextsToRemove.forEach(context => {
      this.loadingStates.get(context)?.complete();
      this.loadingStates.delete(context);
      this.loadingMessages.delete(context);
    });
  }

  /**
   * Obtiene estadísticas de uso del loading manager
   */
  getStats(): {
    totalContexts: number;
    activeContexts: number;
    contexts: Array<{context: string, active: boolean, message?: string}>;
  } {
    const contexts: Array<{context: string, active: boolean, message?: string}> = [];

    this.loadingStates.forEach((state, context) => {
      contexts.push({
        context,
        active: state.value,
        message: this.loadingMessages.get(context)
      });
    });

    return {
      totalContexts: this.loadingStates.size,
      activeContexts: this.getActiveLoadingContexts().length,
      contexts
    };
  }
}
