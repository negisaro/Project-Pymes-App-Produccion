import { Component, Input, OnInit } from '@angular/core';
import { HeroSectionConfig, HeroAction, Breadcrumb, StatCard } from '../../../interfaces/data-management.interface';

/**
 * Componente reutilizable para secciones hero/header
 * Proporciona encabezados consistentes con breadcrumbs, acciones y estadísticas
 */
@Component({
  selector: 'app-hero-section',
  templateUrl: './hero-section.component.html',
  styleUrls: ['./hero-section.component.css']
})
export class HeroSectionComponent implements OnInit {

  @Input() config!: HeroSectionConfig;
  @Input() loading: boolean = false;
  @Input() variant: 'default' | 'minimal' | 'card' | 'gradient' = 'default';
  @Input() size: 'sm' | 'md' | 'lg' = 'md';

  constructor() { }

  ngOnInit(): void {
    // Configuración por defecto si no se proporciona
    if (!this.config) {
      this.config = {
        title: 'Título por Defecto',
        subtitle: 'Subtítulo',
        description: 'Descripción del módulo'
      };
    }
  }

  /**
   * Ejecuta acción del hero
   */
  onActionClick(action: HeroAction): void {
    if (action.handler && !action.disabled && action.visible !== false) {
      action.handler();
    }
  }

  /**
   * Verifica si una acción es visible
   */
  isActionVisible(action: HeroAction): boolean {
    return action.visible !== false;
  }

  /**
   * Verifica si una acción está deshabilitada
   */
  isActionDisabled(action: HeroAction): boolean {
    return action.disabled === true;
  }

  /**
   * Obtiene clases CSS para el contenedor principal
   */
  getContainerClasses(): string {
    const classes = ['hero-section'];

    classes.push(`hero-section--${this.variant}`);
    classes.push(`hero-section--${this.size}`);

    if (this.loading) {
      classes.push('hero-section--loading');
    }

    if (this.config.gradient) {
      classes.push('hero-section--gradient');
    }

    return classes.join(' ');
  }

  /**
   * Obtiene estilos dinámicos para gradiente
   */
  getGradientStyles(): any {
    if (!this.config.gradient) {
      return {};
    }

    return {
      background: this.config.gradient
    };
  }

  /**
   * Obtiene clases para el trending de estadísticas
   */
  getTrendClasses(trend: StatCard['trend']): string {
    if (!trend) return '';

    const classes = ['trend'];
    classes.push(`trend--${trend.direction}`);

    return classes.join(' ');
  }

  /**
   * Obtiene el icono de trending
   */
  getTrendIcon(direction: 'up' | 'down'): string {
    return direction === 'up' ? 'bi bi-arrow-up' : 'bi bi-arrow-down';
  }
}
