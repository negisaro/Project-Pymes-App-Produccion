import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// Reexportados desde barrel components/index.ts para rutas más limpias
import { UIButtonComponent, UIBadgeComponent } from './components';

@NgModule({
  declarations: [UIButtonComponent, UIBadgeComponent],
  imports: [CommonModule],
  exports: [UIButtonComponent, UIBadgeComponent]
})
export class UiKitModule {}
