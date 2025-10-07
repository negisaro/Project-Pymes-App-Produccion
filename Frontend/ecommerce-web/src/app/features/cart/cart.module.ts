import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { CartRoutingModule } from './cart-routing.module';

// 🧠 Core Domain Exports
import { CartFacade } from './presentation/facades/cart.facade';

// 🔧 Infrastructure Providers
import { CartStateService } from './infrastructure/state/cart-state.service';
import { CartCacheService } from './infrastructure/storage/cart-cache.service';
import { HttpCartRepository } from './infrastructure/api/http-cart.repository';
import { CartMapper } from './infrastructure/mappers/cart.mapper';

// 🎯 Use Cases
import { AddItemToCartUseCase } from './core/use-cases/add-item-to-cart.use-case';
import { UpdateCartItemQuantityUseCase } from './core/use-cases/update-cart-item-quantity.use-case';
import { RemoveCartItemUseCase } from './core/use-cases/remove-cart-item.use-case';
import { CalculateCartTotalsUseCase } from './core/use-cases/calculate-cart-totals.use-case';

// 📦 Repository Token
import { CART_REPOSITORY_TOKEN } from './core/repositories/cart.repository';

// 🎨 Presentation Components
import { CartItemComponent } from './presentation/components/cart-item/cart-item.component';
import { CartSummaryComponent } from './presentation/components/cart-summary/cart-summary.component';
// Usar solo los componentes de shared

@NgModule({
  declarations: [
    // Componentes de presentación
    CartItemComponent,
    CartSummaryComponent,
  // Usar solo los componentes de shared
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CartRoutingModule
  ],
  providers: [
    // 🎭 Facade Layer
    CartFacade,

    // 🧠 Domain Use Cases
    AddItemToCartUseCase,
    UpdateCartItemQuantityUseCase,
    RemoveCartItemUseCase,
    CalculateCartTotalsUseCase,

    // 🔧 Infrastructure Services
    CartStateService,
    CartCacheService,
    CartMapper,

    // 📦 Repository Implementation
    {
      provide: CART_REPOSITORY_TOKEN,
      useClass: HttpCartRepository
    }
  ],
  exports: [
    // Componentes que pueden ser usados externamente
    CartItemComponent,
    CartSummaryComponent,
  // Usar solo los componentes de shared
  ]
})
export class CartModule {

  /**
   * Configuración inicial del módulo Cart
   * Se ejecuta una sola vez cuando se carga el módulo
   */
  constructor() {
    console.log('🛒 Cart Module initialized with Clean Architecture');
  }
}
