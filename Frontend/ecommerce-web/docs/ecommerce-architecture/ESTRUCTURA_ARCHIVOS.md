# 📁 Estructura de Archivos E-Commerce Completa

## 📋 Descripción

Estructura completa de archivos y carpetas para la aplicación E-Commerce PYMES basada en la arquitectura modular documentada.

## 🏗️ **Estructura del Proyecto**

```
ecommerce-web/
├── angular.json
├── package.json
├── tsconfig.json
├── tsconfig.app.json
├── tsconfig.spec.json
├── Dockerfile
├── nginx.conf
├── README.md
├── .gitignore
├── .editorconfig
├── .eslintrc.json
├── .prettierrc
├── karma.conf.js
├── protractor.conf.js
│
├── public/
│   ├── favicon.ico
│   ├── robots.txt
│   ├── sitemap.xml
│   └── manifest.json
│
├── src/
│   ├── index.html
│   ├── main.ts
│   ├── polyfills.ts
│   ├── styles.scss
│   ├── test.ts
│   │
│   ├── environments/
│   │   ├── environment.ts
│   │   ├── environment.prod.ts
│   │   ├── environment.staging.ts
│   │   └── environment.test.ts
│   │
│   ├── assets/
│   │   ├── images/
│   │   │   ├── logo/
│   │   │   │   ├── logo.svg
│   │   │   │   ├── logo-dark.svg
│   │   │   │   └── favicon.png
│   │   │   ├── products/
│   │   │   │   ├── placeholders/
│   │   │   │   └── categories/
│   │   │   ├── icons/
│   │   │   │   ├── payment/
│   │   │   │   ├── shipping/
│   │   │   │   └── social/
│   │   │   └── backgrounds/
│   │   │       ├── hero-bg.jpg
│   │   │       └── pattern-bg.svg
│   │   ├── fonts/
│   │   │   ├── Inter/
│   │   │   └── Poppins/
│   │   ├── styles/
│   │   │   ├── base/
│   │   │   │   ├── _reset.scss
│   │   │   │   ├── _typography.scss
│   │   │   │   └── _variables.scss
│   │   │   ├── components/
│   │   │   │   ├── _buttons.scss
│   │   │   │   ├── _forms.scss
│   │   │   │   └── _cards.scss
│   │   │   └── utilities/
│   │   │       ├── _spacing.scss
│   │   │       └── _mixins.scss
│   │   └── js/
│   │       ├── analytics.js
│   │       └── external-libs.js
│   │
│   └── app/
│       ├── app.module.ts
│       ├── app.component.ts
│       ├── app.component.html
│       ├── app.component.scss
│       ├── app.component.spec.ts
│       ├── app-routing.module.ts
│       │
│       ├── core/
│       │   ├── core.module.ts
│       │   ├── guards/
│       │   │   ├── auth.guard.ts
│       │   │   ├── role.guard.ts
│       │   │   ├── can-deactivate.guard.ts
│       │   │   └── admin.guard.ts
│       │   ├── interceptors/
│       │   │   ├── auth.interceptor.ts
│       │   │   ├── error.interceptor.ts
│       │   │   ├── loading.interceptor.ts
│       │   │   └── cache.interceptor.ts
│       │   ├── services/
│       │   │   ├── auth.service.ts
│       │   │   ├── http.service.ts
│       │   │   ├── storage.service.ts
│       │   │   ├── error-handler.service.ts
│       │   │   ├── notification.service.ts
│       │   │   ├── logger.service.ts
│       │   │   ├── meta.service.ts
│       │   │   └── pwa.service.ts
│       │   ├── models/
│       │   │   ├── user.model.ts
│       │   │   ├── api-response.model.ts
│       │   │   ├── pagination.model.ts
│       │   │   └── error.model.ts
│       │   ├── constants/
│       │   │   ├── api-endpoints.ts
│       │   │   ├── app-constants.ts
│       │   │   └── storage-keys.ts
│       │   └── config/
│       │       ├── app.config.ts
│       │       └── jwt.config.ts
│       │
│       ├── shared/
│       │   ├── shared.module.ts
│       │   ├── components/
│       │   │   ├── layout/
│       │   │   │   ├── header/
│       │   │   │   │   ├── header.component.ts
│       │   │   │   │   ├── header.component.html
│       │   │   │   │   ├── header.component.scss
│       │   │   │   │   └── header.component.spec.ts
│       │   │   │   ├── footer/
│       │   │   │   │   ├── footer.component.ts
│       │   │   │   │   ├── footer.component.html
│       │   │   │   │   ├── footer.component.scss
│       │   │   │   │   └── footer.component.spec.ts
│       │   │   │   ├── sidebar/
│       │   │   │   │   ├── sidebar.component.ts
│       │   │   │   │   ├── sidebar.component.html
│       │   │   │   │   ├── sidebar.component.scss
│       │   │   │   │   └── sidebar.component.spec.ts
│       │   │   │   └── navigation/
│       │   │   │       ├── breadcrumb/
│       │   │   │       │   ├── breadcrumb.component.ts
│       │   │   │       │   ├── breadcrumb.component.html
│       │   │   │       │   ├── breadcrumb.component.scss
│       │   │   │       │   └── breadcrumb.component.spec.ts
│       │   │   │       └── menu/
│       │   │   │           ├── menu.component.ts
│       │   │   │           ├── menu.component.html
│       │   │   │           ├── menu.component.scss
│       │   │   │           └── menu.component.spec.ts
│       │   │   ├── ui/
│       │   │   │   ├── button/
│       │   │   │   │   ├── button.component.ts
│       │   │   │   │   ├── button.component.html
│       │   │   │   │   ├── button.component.scss
│       │   │   │   │   └── button.component.spec.ts
│       │   │   │   ├── input/
│       │   │   │   │   ├── input.component.ts
│       │   │   │   │   ├── input.component.html
│       │   │   │   │   ├── input.component.scss
│       │   │   │   │   └── input.component.spec.ts
│       │   │   │   ├── card/
│       │   │   │   │   ├── card.component.ts
│       │   │   │   │   ├── card.component.html
│       │   │   │   │   ├── card.component.scss
│       │   │   │   │   └── card.component.spec.ts
│       │   │   │   ├── modal/
│       │   │   │   │   ├── modal.component.ts
│       │   │   │   │   ├── modal.component.html
│       │   │   │   │   ├── modal.component.scss
│       │   │   │   │   └── modal.component.spec.ts
│       │   │   │   ├── loading/
│       │   │   │   │   ├── spinner/
│       │   │   │   │   │   ├── spinner.component.ts
│       │   │   │   │   │   ├── spinner.component.html
│       │   │   │   │   │   ├── spinner.component.scss
│       │   │   │   │   │   └── spinner.component.spec.ts
│       │   │   │   │   └── skeleton/
│       │   │   │   │       ├── skeleton.component.ts
│       │   │   │   │       ├── skeleton.component.html
│       │   │   │   │       ├── skeleton.component.scss
│       │   │   │   │       └── skeleton.component.spec.ts
│       │   │   │   ├── feedback/
│       │   │   │   │   ├── alert/
│       │   │   │   │   │   ├── alert.component.ts
│       │   │   │   │   │   ├── alert.component.html
│       │   │   │   │   │   ├── alert.component.scss
│       │   │   │   │   │   └── alert.component.spec.ts
│       │   │   │   │   ├── toast/
│       │   │   │   │   │   ├── toast.component.ts
│       │   │   │   │   │   ├── toast.component.html
│       │   │   │   │   │   ├── toast.component.scss
│       │   │   │   │   │   └── toast.component.spec.ts
│       │   │   │   │   └── progress/
│       │   │   │   │       ├── progress-bar.component.ts
│       │   │   │   │       ├── progress-bar.component.html
│       │   │   │   │       ├── progress-bar.component.scss
│       │   │   │   │       └── progress-bar.component.spec.ts
│       │   │   │   └── form/
│       │   │   │       ├── select/
│       │   │   │       │   ├── select.component.ts
│       │   │   │       │   ├── select.component.html
│       │   │   │       │   ├── select.component.scss
│       │   │   │       │   └── select.component.spec.ts
│       │   │   │       ├── checkbox/
│       │   │   │       │   ├── checkbox.component.ts
│       │   │   │       │   ├── checkbox.component.html
│       │   │   │       │   ├── checkbox.component.scss
│       │   │   │       │   └── checkbox.component.spec.ts
│       │   │   │       ├── radio/
│       │   │   │       │   ├── radio.component.ts
│       │   │   │       │   ├── radio.component.html
│       │   │   │       │   ├── radio.component.scss
│       │   │   │       │   └── radio.component.spec.ts
│       │   │   │       └── rating/
│       │   │   │           ├── rating.component.ts
│       │   │   │           ├── rating.component.html
│       │   │   │           ├── rating.component.scss
│       │   │   │           └── rating.component.spec.ts
│       │   │   └── common/
│       │   │       ├── empty-state/
│       │   │       │   ├── empty-state.component.ts
│       │   │       │   ├── empty-state.component.html
│       │   │       │   ├── empty-state.component.scss
│       │   │       │   └── empty-state.component.spec.ts
│       │   │       ├── pagination/
│       │   │       │   ├── pagination.component.ts
│       │   │       │   ├── pagination.component.html
│       │   │       │   ├── pagination.component.scss
│       │   │       │   └── pagination.component.spec.ts
│       │   │       ├── search/
│       │   │       │   ├── search-box.component.ts
│       │   │       │   ├── search-box.component.html
│       │   │       │   ├── search-box.component.scss
│       │   │       │   └── search-box.component.spec.ts
│       │   │       └── confirmation/
│       │   │           ├── confirmation-dialog.component.ts
│       │   │           ├── confirmation-dialog.component.html
│       │   │           ├── confirmation-dialog.component.scss
│       │   │           └── confirmation-dialog.component.spec.ts
│       │   ├── pipes/
│       │   │   ├── currency.pipe.ts
│       │   │   ├── date-ago.pipe.ts
│       │   │   ├── truncate.pipe.ts
│       │   │   ├── safe-html.pipe.ts
│       │   │   └── highlight.pipe.ts
│       │   ├── directives/
│       │   │   ├── auto-focus.directive.ts
│       │   │   ├── click-outside.directive.ts
│       │   │   ├── lazy-load.directive.ts
│       │   │   ├── tooltip.directive.ts
│       │   │   └── intersection-observer.directive.ts
│       │   ├── validators/
│       │   │   ├── custom-validators.ts
│       │   │   ├── email-validator.ts
│       │   │   ├── password-validator.ts
│       │   │   └── phone-validator.ts
│       │   └── utils/
│       │       ├── form.utils.ts
│       │       ├── date.utils.ts
│       │       ├── string.utils.ts
│       │       ├── number.utils.ts
│       │       └── validation.utils.ts
│       │
│       ├── features/
│       │   ├── home/
│       │   │   ├── home.module.ts
│       │   │   ├── home-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── landing/
│       │   │   │   │   ├── landing.component.ts
│       │   │   │   │   ├── landing.component.html
│       │   │   │   │   ├── landing.component.scss
│       │   │   │   │   └── landing.component.spec.ts
│       │   │   │   └── about/
│       │   │   │       ├── about.component.ts
│       │   │   │       ├── about.component.html
│       │   │   │       ├── about.component.scss
│       │   │   │       └── about.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── hero-section/
│       │   │   │   │   ├── hero-section.component.ts
│       │   │   │   │   ├── hero-section.component.html
│       │   │   │   │   ├── hero-section.component.scss
│       │   │   │   │   └── hero-section.component.spec.ts
│       │   │   │   ├── featured-products/
│       │   │   │   │   ├── featured-products.component.ts
│       │   │   │   │   ├── featured-products.component.html
│       │   │   │   │   ├── featured-products.component.scss
│       │   │   │   │   └── featured-products.component.spec.ts
│       │   │   │   ├── category-showcase/
│       │   │   │   │   ├── category-showcase.component.ts
│       │   │   │   │   ├── category-showcase.component.html
│       │   │   │   │   ├── category-showcase.component.scss
│       │   │   │   │   └── category-showcase.component.spec.ts
│       │   │   │   └── value-propositions/
│       │   │   │       ├── value-propositions.component.ts
│       │   │   │       ├── value-propositions.component.html
│       │   │   │       ├── value-propositions.component.scss
│       │   │   │       └── value-propositions.component.spec.ts
│       │   │   └── services/
│       │   │       └── home.service.ts
│       │   │
│       │   ├── auth/
│       │   │   ├── auth.module.ts
│       │   │   ├── auth-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── login/
│       │   │   │   │   ├── login.component.ts
│       │   │   │   │   ├── login.component.html
│       │   │   │   │   ├── login.component.scss
│       │   │   │   │   └── login.component.spec.ts
│       │   │   │   ├── register/
│       │   │   │   │   ├── register.component.ts
│       │   │   │   │   ├── register.component.html
│       │   │   │   │   ├── register.component.scss
│       │   │   │   │   └── register.component.spec.ts
│       │   │   │   ├── forgot-password/
│       │   │   │   │   ├── forgot-password.component.ts
│       │   │   │   │   ├── forgot-password.component.html
│       │   │   │   │   ├── forgot-password.component.scss
│       │   │   │   │   └── forgot-password.component.spec.ts
│       │   │   │   └── reset-password/
│       │   │   │       ├── reset-password.component.ts
│       │   │   │       ├── reset-password.component.html
│       │   │   │       ├── reset-password.component.scss
│       │   │   │       └── reset-password.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── auth-form/
│       │   │   │   │   ├── auth-form.component.ts
│       │   │   │   │   ├── auth-form.component.html
│       │   │   │   │   ├── auth-form.component.scss
│       │   │   │   │   └── auth-form.component.spec.ts
│       │   │   │   └── social-login/
│       │   │   │       ├── social-login.component.ts
│       │   │   │       ├── social-login.component.html
│       │   │   │       ├── social-login.component.scss
│       │   │   │       └── social-login.component.spec.ts
│       │   │   ├── services/
│       │   │   │   ├── auth.service.ts
│       │   │   │   ├── token.service.ts
│       │   │   │   └── user.service.ts
│       │   │   └── models/
│       │   │       ├── login.model.ts
│       │   │       ├── register.model.ts
│       │   │       └── user-profile.model.ts
│       │   │
│       │   ├── categoria/
│       │   │   ├── categoria.module.ts
│       │   │   ├── categoria-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── categoria-list/
│       │   │   │   │   ├── categoria-list.component.ts
│       │   │   │   │   ├── categoria-list.component.html
│       │   │   │   │   ├── categoria-list.component.scss
│       │   │   │   │   └── categoria-list.component.spec.ts
│       │   │   │   └── categoria-detail/
│       │   │   │       ├── categoria-detail.component.ts
│       │   │   │       ├── categoria-detail.component.html
│       │   │   │       ├── categoria-detail.component.scss
│       │   │   │       └── categoria-detail.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── categoria-card/
│       │   │   │   │   ├── categoria-card.component.ts
│       │   │   │   │   ├── categoria-card.component.html
│       │   │   │   │   ├── categoria-card.component.scss
│       │   │   │   │   └── categoria-card.component.spec.ts
│       │   │   │   └── categoria-filter/
│       │   │   │       ├── categoria-filter.component.ts
│       │   │   │       ├── categoria-filter.component.html
│       │   │   │       ├── categoria-filter.component.scss
│       │   │   │       └── categoria-filter.component.spec.ts
│       │   │   ├── services/
│       │   │   │   └── categoria.service.ts
│       │   │   └── models/
│       │   │       └── categoria.model.ts
│       │   │
│       │   ├── producto/
│       │   │   ├── producto.module.ts
│       │   │   ├── producto-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── product-list/
│       │   │   │   │   ├── product-list.component.ts
│       │   │   │   │   ├── product-list.component.html
│       │   │   │   │   ├── product-list.component.scss
│       │   │   │   │   └── product-list.component.spec.ts
│       │   │   │   ├── product-detail/
│       │   │   │   │   ├── product-detail.component.ts
│       │   │   │   │   ├── product-detail.component.html
│       │   │   │   │   ├── product-detail.component.scss
│       │   │   │   │   └── product-detail.component.spec.ts
│       │   │   │   └── product-compare/
│       │   │   │       ├── product-compare.component.ts
│       │   │   │       ├── product-compare.component.html
│       │   │   │       ├── product-compare.component.scss
│       │   │   │       └── product-compare.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── product-card/
│       │   │   │   │   ├── product-card.component.ts
│       │   │   │   │   ├── product-card.component.html
│       │   │   │   │   ├── product-card.component.scss
│       │   │   │   │   └── product-card.component.spec.ts
│       │   │   │   ├── product-filter/
│       │   │   │   │   ├── product-filter.component.ts
│       │   │   │   │   ├── product-filter.component.html
│       │   │   │   │   ├── product-filter.component.scss
│       │   │   │   │   └── product-filter.component.spec.ts
│       │   │   │   ├── product-gallery/
│       │   │   │   │   ├── product-gallery.component.ts
│       │   │   │   │   ├── product-gallery.component.html
│       │   │   │   │   ├── product-gallery.component.scss
│       │   │   │   │   └── product-gallery.component.spec.ts
│       │   │   │   ├── product-reviews/
│       │   │   │   │   ├── product-reviews.component.ts
│       │   │   │   │   ├── product-reviews.component.html
│       │   │   │   │   ├── product-reviews.component.scss
│       │   │   │   │   └── product-reviews.component.spec.ts
│       │   │   │   └── quick-view/
│       │   │   │       ├── quick-view.component.ts
│       │   │   │       ├── quick-view.component.html
│       │   │   │       ├── quick-view.component.scss
│       │   │   │       └── quick-view.component.spec.ts
│       │   │   ├── services/
│       │   │   │   ├── product.service.ts
│       │   │   │   ├── product-search.service.ts
│       │   │   │   └── product-review.service.ts
│       │   │   └── models/
│       │   │       ├── product.model.ts
│       │   │       ├── product-filter.model.ts
│       │   │       └── product-review.model.ts
│       │   │
│       │   ├── cart/
│       │   │   ├── cart.module.ts
│       │   │   ├── cart-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── shopping-cart/
│       │   │   │   │   ├── shopping-cart.component.ts
│       │   │   │   │   ├── shopping-cart.component.html
│       │   │   │   │   ├── shopping-cart.component.scss
│       │   │   │   │   └── shopping-cart.component.spec.ts
│       │   │   │   ├── checkout/
│       │   │   │   │   ├── checkout.component.ts
│       │   │   │   │   ├── checkout.component.html
│       │   │   │   │   ├── checkout.component.scss
│       │   │   │   │   └── checkout.component.spec.ts
│       │   │   │   └── order-confirmation/
│       │   │   │       ├── order-confirmation.component.ts
│       │   │   │       ├── order-confirmation.component.html
│       │   │   │       ├── order-confirmation.component.scss
│       │   │   │       └── order-confirmation.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── cart-item/
│       │   │   │   │   ├── cart-item.component.ts
│       │   │   │   │   ├── cart-item.component.html
│       │   │   │   │   ├── cart-item.component.scss
│       │   │   │   │   └── cart-item.component.spec.ts
│       │   │   │   ├── cart-summary/
│       │   │   │   │   ├── cart-summary.component.ts
│       │   │   │   │   ├── cart-summary.component.html
│       │   │   │   │   ├── cart-summary.component.scss
│       │   │   │   │   └── cart-summary.component.spec.ts
│       │   │   │   ├── mini-cart/
│       │   │   │   │   ├── mini-cart.component.ts
│       │   │   │   │   ├── mini-cart.component.html
│       │   │   │   │   ├── mini-cart.component.scss
│       │   │   │   │   └── mini-cart.component.spec.ts
│       │   │   │   ├── checkout-steps/
│       │   │   │   │   ├── shipping-info/
│       │   │   │   │   │   ├── shipping-info.component.ts
│       │   │   │   │   │   ├── shipping-info.component.html
│       │   │   │   │   │   ├── shipping-info.component.scss
│       │   │   │   │   │   └── shipping-info.component.spec.ts
│       │   │   │   │   ├── payment-info/
│       │   │   │   │   │   ├── payment-info.component.ts
│       │   │   │   │   │   ├── payment-info.component.html
│       │   │   │   │   │   ├── payment-info.component.scss
│       │   │   │   │   │   └── payment-info.component.spec.ts
│       │   │   │   │   └── order-review/
│       │   │   │   │       ├── order-review.component.ts
│       │   │   │   │       ├── order-review.component.html
│       │   │   │   │       ├── order-review.component.scss
│       │   │   │   │       └── order-review.component.spec.ts
│       │   │   │   └── coupon/
│       │   │   │       ├── coupon.component.ts
│       │   │   │       ├── coupon.component.html
│       │   │   │       ├── coupon.component.scss
│       │   │   │       └── coupon.component.spec.ts
│       │   │   ├── services/
│       │   │   │   ├── cart.service.ts
│       │   │   │   ├── checkout.service.ts
│       │   │   │   ├── shipping.service.ts
│       │   │   │   └── coupon.service.ts
│       │   │   └── models/
│       │   │       ├── cart.model.ts
│       │   │       ├── cart-item.model.ts
│       │   │       ├── checkout.model.ts
│       │   │       ├── shipping.model.ts
│       │   │       └── coupon.model.ts
│       │   │
│       │   ├── order/
│       │   │   ├── order.module.ts
│       │   │   ├── order-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── order-list/
│       │   │   │   │   ├── order-list.component.ts
│       │   │   │   │   ├── order-list.component.html
│       │   │   │   │   ├── order-list.component.scss
│       │   │   │   │   └── order-list.component.spec.ts
│       │   │   │   ├── order-detail/
│       │   │   │   │   ├── order-detail.component.ts
│       │   │   │   │   ├── order-detail.component.html
│       │   │   │   │   ├── order-detail.component.scss
│       │   │   │   │   └── order-detail.component.spec.ts
│       │   │   │   └── order-tracking/
│       │   │   │       ├── order-tracking.component.ts
│       │   │   │       ├── order-tracking.component.html
│       │   │   │       ├── order-tracking.component.scss
│       │   │   │       └── order-tracking.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── order-card/
│       │   │   │   │   ├── order-card.component.ts
│       │   │   │   │   ├── order-card.component.html
│       │   │   │   │   ├── order-card.component.scss
│       │   │   │   │   └── order-card.component.spec.ts
│       │   │   │   ├── order-status/
│       │   │   │   │   ├── order-status.component.ts
│       │   │   │   │   ├── order-status.component.html
│       │   │   │   │   ├── order-status.component.scss
│       │   │   │   │   └── order-status.component.spec.ts
│       │   │   │   └── tracking-timeline/
│       │   │   │       ├── tracking-timeline.component.ts
│       │   │   │       ├── tracking-timeline.component.html
│       │   │   │       ├── tracking-timeline.component.scss
│       │   │   │       └── tracking-timeline.component.spec.ts
│       │   │   ├── services/
│       │   │   │   ├── order.service.ts
│       │   │   │   └── tracking.service.ts
│       │   │   └── models/
│       │   │       ├── order.model.ts
│       │   │       └── tracking.model.ts
│       │   │
│       │   ├── user/
│       │   │   ├── user.module.ts
│       │   │   ├── user-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── profile/
│       │   │   │   │   ├── profile.component.ts
│       │   │   │   │   ├── profile.component.html
│       │   │   │   │   ├── profile.component.scss
│       │   │   │   │   └── profile.component.spec.ts
│       │   │   │   ├── dashboard/
│       │   │   │   │   ├── dashboard.component.ts
│       │   │   │   │   ├── dashboard.component.html
│       │   │   │   │   ├── dashboard.component.scss
│       │   │   │   │   └── dashboard.component.spec.ts
│       │   │   │   ├── settings/
│       │   │   │   │   ├── settings.component.ts
│       │   │   │   │   ├── settings.component.html
│       │   │   │   │   ├── settings.component.scss
│       │   │   │   │   └── settings.component.spec.ts
│       │   │   │   ├── addresses/
│       │   │   │   │   ├── addresses.component.ts
│       │   │   │   │   ├── addresses.component.html
│       │   │   │   │   ├── addresses.component.scss
│       │   │   │   │   └── addresses.component.spec.ts
│       │   │   │   └── wishlist/
│       │   │   │       ├── wishlist.component.ts
│       │   │   │       ├── wishlist.component.html
│       │   │   │       ├── wishlist.component.scss
│       │   │   │       └── wishlist.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── user-menu/
│       │   │   │   │   ├── user-menu.component.ts
│       │   │   │   │   ├── user-menu.component.html
│       │   │   │   │   ├── user-menu.component.scss
│       │   │   │   │   └── user-menu.component.spec.ts
│       │   │   │   ├── profile-form/
│       │   │   │   │   ├── profile-form.component.ts
│       │   │   │   │   ├── profile-form.component.html
│       │   │   │   │   ├── profile-form.component.scss
│       │   │   │   │   └── profile-form.component.spec.ts
│       │   │   │   └── address-form/
│       │   │   │       ├── address-form.component.ts
│       │   │   │       ├── address-form.component.html
│       │   │   │       ├── address-form.component.scss
│       │   │   │       └── address-form.component.spec.ts
│       │   │   ├── services/
│       │   │   │   ├── user.service.ts
│       │   │   │   ├── address.service.ts
│       │   │   │   └── wishlist.service.ts
│       │   │   └── models/
│       │   │       ├── user.model.ts
│       │   │       ├── address.model.ts
│       │   │       └── wishlist.model.ts
│       │   │
│       │   ├── search/
│       │   │   ├── search.module.ts
│       │   │   ├── search-routing.module.ts
│       │   │   ├── pages/
│       │   │   │   ├── search-results/
│       │   │   │   │   ├── search-results.component.ts
│       │   │   │   │   ├── search-results.component.html
│       │   │   │   │   ├── search-results.component.scss
│       │   │   │   │   └── search-results.component.spec.ts
│       │   │   │   └── advanced-search/
│       │   │   │       ├── advanced-search.component.ts
│       │   │   │       ├── advanced-search.component.html
│       │   │   │       ├── advanced-search.component.scss
│       │   │   │       └── advanced-search.component.spec.ts
│       │   │   ├── components/
│       │   │   │   ├── search-filters/
│       │   │   │   │   ├── search-filters.component.ts
│       │   │   │   │   ├── search-filters.component.html
│       │   │   │   │   ├── search-filters.component.scss
│       │   │   │   │   └── search-filters.component.spec.ts
│       │   │   │   ├── search-suggestions/
│       │   │   │   │   ├── search-suggestions.component.ts
│       │   │   │   │   ├── search-suggestions.component.html
│       │   │   │   │   ├── search-suggestions.component.scss
│       │   │   │   │   └── search-suggestions.component.spec.ts
│       │   │   │   └── search-history/
│       │   │   │       ├── search-history.component.ts
│       │   │   │       ├── search-history.component.html
│       │   │   │       ├── search-history.component.scss
│       │   │   │       └── search-history.component.spec.ts
│       │   │   ├── services/
│       │   │   │   └── search.service.ts
│       │   │   └── models/
│       │   │       ├── search.model.ts
│       │   │       └── search-filter.model.ts
│       │   │
│       │   └── admin/
│       │       ├── admin.module.ts
│       │       ├── admin-routing.module.ts
│       │       ├── pages/
│       │       │   ├── dashboard/
│       │       │   │   ├── admin-dashboard.component.ts
│       │       │   │   ├── admin-dashboard.component.html
│       │       │   │   ├── admin-dashboard.component.scss
│       │       │   │   └── admin-dashboard.component.spec.ts
│       │       │   ├── product-management/
│       │       │   │   ├── product-management.component.ts
│       │       │   │   ├── product-management.component.html
│       │       │   │   ├── product-management.component.scss
│       │       │   │   └── product-management.component.spec.ts
│       │       │   ├── category-management/
│       │       │   │   ├── category-management.component.ts
│       │       │   │   ├── category-management.component.html
│       │       │   │   ├── category-management.component.scss
│       │       │   │   └── category-management.component.spec.ts
│       │       │   ├── order-management/
│       │       │   │   ├── order-management.component.ts
│       │       │   │   ├── order-management.component.html
│       │       │   │   ├── order-management.component.scss
│       │       │   │   └── order-management.component.spec.ts
│       │       │   ├── user-management/
│       │       │   │   ├── user-management.component.ts
│       │       │   │   ├── user-management.component.html
│       │       │   │   ├── user-management.component.scss
│       │       │   │   └── user-management.component.spec.ts
│       │       │   ├── analytics/
│       │       │   │   ├── analytics.component.ts
│       │       │   │   ├── analytics.component.html
│       │       │   │   ├── analytics.component.scss
│       │       │   │   └── analytics.component.spec.ts
│       │       │   └── settings/
│       │       │       ├── admin-settings.component.ts
│       │       │       ├── admin-settings.component.html
│       │       │       ├── admin-settings.component.scss
│       │       │       └── admin-settings.component.spec.ts
│       │       ├── components/
│       │       │   ├── admin-sidebar/
│       │       │   │   ├── admin-sidebar.component.ts
│       │       │   │   ├── admin-sidebar.component.html
│       │       │   │   ├── admin-sidebar.component.scss
│       │       │   │   └── admin-sidebar.component.spec.ts
│       │       │   ├── stats-card/
│       │       │   │   ├── stats-card.component.ts
│       │       │   │   ├── stats-card.component.html
│       │       │   │   ├── stats-card.component.scss
│       │       │   │   └── stats-card.component.spec.ts
│       │       │   ├── data-table/
│       │       │   │   ├── data-table.component.ts
│       │       │   │   ├── data-table.component.html
│       │       │   │   ├── data-table.component.scss
│       │       │   │   └── data-table.component.spec.ts
│       │       │   └── chart/
│       │       │       ├── chart.component.ts
│       │       │       ├── chart.component.html
│       │       │       ├── chart.component.scss
│       │       │       └── chart.component.spec.ts
│       │       ├── services/
│       │       │   ├── admin.service.ts
│       │       │   ├── analytics.service.ts
│       │       │   └── admin-user.service.ts
│       │       └── models/
│       │           ├── admin.model.ts
│       │           ├── analytics.model.ts
│       │           └── stats.model.ts
│       │
│       ├── state/
│       │   ├── app.state.ts
│       │   ├── app.effects.ts
│       │   ├── app.reducer.ts
│       │   ├── app.selectors.ts
│       │   ├── auth/
│       │   │   ├── auth.actions.ts
│       │   │   ├── auth.effects.ts
│       │   │   ├── auth.reducer.ts
│       │   │   ├── auth.selectors.ts
│       │   │   └── auth.state.ts
│       │   ├── cart/
│       │   │   ├── cart.actions.ts
│       │   │   ├── cart.effects.ts
│       │   │   ├── cart.reducer.ts
│       │   │   ├── cart.selectors.ts
│       │   │   └── cart.state.ts
│       │   ├── product/
│       │   │   ├── product.actions.ts
│       │   │   ├── product.effects.ts
│       │   │   ├── product.reducer.ts
│       │   │   ├── product.selectors.ts
│       │   │   └── product.state.ts
│       │   ├── category/
│       │   │   ├── category.actions.ts
│       │   │   ├── category.effects.ts
│       │   │   ├── category.reducer.ts
│       │   │   ├── category.selectors.ts
│       │   │   └── category.state.ts
│       │   ├── order/
│       │   │   ├── order.actions.ts
│       │   │   ├── order.effects.ts
│       │   │   ├── order.reducer.ts
│       │   │   ├── order.selectors.ts
│       │   │   └── order.state.ts
│       │   └── user/
│       │       ├── user.actions.ts
│       │       ├── user.effects.ts
│       │       ├── user.reducer.ts
│       │       ├── user.selectors.ts
│       │       └── user.state.ts
│       │
│       └── ui-kit/
│           ├── ui-kit.module.ts
│           ├── components/
│           │   ├── atoms/
│           │   │   ├── icon/
│           │   │   │   ├── icon.component.ts
│           │   │   │   ├── icon.component.html
│           │   │   │   ├── icon.component.scss
│           │   │   │   └── icon.component.spec.ts
│           │   │   ├── badge/
│           │   │   │   ├── badge.component.ts
│           │   │   │   ├── badge.component.html
│           │   │   │   ├── badge.component.scss
│           │   │   │   └── badge.component.spec.ts
│           │   │   ├── avatar/
│           │   │   │   ├── avatar.component.ts
│           │   │   │   ├── avatar.component.html
│           │   │   │   ├── avatar.component.scss
│           │   │   │   └── avatar.component.spec.ts
│           │   │   └── divider/
│           │   │       ├── divider.component.ts
│           │   │       ├── divider.component.html
│           │   │       ├── divider.component.scss
│           │   │       └── divider.component.spec.ts
│           │   ├── molecules/
│           │   │   ├── dropdown/
│           │   │   │   ├── dropdown.component.ts
│           │   │   │   ├── dropdown.component.html
│           │   │   │   ├── dropdown.component.scss
│           │   │   │   └── dropdown.component.spec.ts
│           │   │   ├── tabs/
│           │   │   │   ├── tabs.component.ts
│           │   │   │   ├── tabs.component.html
│           │   │   │   ├── tabs.component.scss
│           │   │   │   └── tabs.component.spec.ts
│           │   │   ├── accordion/
│           │   │   │   ├── accordion.component.ts
│           │   │   │   ├── accordion.component.html
│           │   │   │   ├── accordion.component.scss
│           │   │   │   └── accordion.component.spec.ts
│           │   │   └── stepper/
│           │   │       ├── stepper.component.ts
│           │   │       ├── stepper.component.html
│           │   │       ├── stepper.component.scss
│           │   │       └── stepper.component.spec.ts
│           │   └── organisms/
│           │       ├── data-grid/
│           │       │   ├── data-grid.component.ts
│           │       │   ├── data-grid.component.html
│           │       │   ├── data-grid.component.scss
│           │       │   └── data-grid.component.spec.ts
│           │       ├── filter-panel/
│           │       │   ├── filter-panel.component.ts
│           │       │   ├── filter-panel.component.html
│           │       │   ├── filter-panel.component.scss
│           │       │   └── filter-panel.component.spec.ts
│           │       └── media-gallery/
│           │           ├── media-gallery.component.ts
│           │           ├── media-gallery.component.html
│           │           ├── media-gallery.component.scss
│           │           └── media-gallery.component.spec.ts
│           ├── tokens/
│           │   ├── colors.ts
│           │   ├── typography.ts
│           │   ├── spacing.ts
│           │   ├── shadows.ts
│           │   ├── borders.ts
│           │   └── animations.ts
│           └── themes/
│               ├── default.theme.ts
│               ├── dark.theme.ts
│               └── theme.service.ts
│
├── docs/
│   ├── README.md
│   ├── CONTRIBUTING.md
│   ├── CHANGELOG.md
│   ├── DEPLOYMENT.md
│   ├── TESTING.md
│   ├── API.md
│   ├── STYLE_GUIDE.md
│   ├── architecture/
│   │   ├── ARCHITECTURE.md
│   │   ├── DECISION_RECORDS.md
│   │   └── PATTERNS.md
│   ├── components/
│   │   ├── COMPONENT_GUIDE.md
│   │   └── UI_KIT.md
│   ├── development/
│   │   ├── SETUP.md
│   │   ├── WORKFLOWS.md
│   │   └── TROUBLESHOOTING.md
│   └── user-guides/
│       ├── ADMIN_GUIDE.md
│       ├── USER_GUIDE.md
│       └── API_GUIDE.md
│
├── e2e/
│   ├── protractor.conf.js
│   ├── src/
│   │   ├── app.e2e-spec.ts
│   │   ├── app.po.ts
│   │   ├── auth/
│   │   │   ├── login.e2e-spec.ts
│   │   │   └── login.po.ts
│   │   ├── product/
│   │   │   ├── product-list.e2e-spec.ts
│   │   │   ├── product-detail.e2e-spec.ts
│   │   │   └── product.po.ts
│   │   ├── cart/
│   │   │   ├── shopping-cart.e2e-spec.ts
│   │   │   ├── checkout.e2e-spec.ts
│   │   │   └── cart.po.ts
│   │   └── admin/
│   │       ├── admin-dashboard.e2e-spec.ts
│   │       └── admin.po.ts
│   └── utils/
│       ├── test-data.ts
│       └── helpers.ts
│
├── scripts/
│   ├── build.sh
│   ├── deploy.sh
│   ├── test.sh
│   ├── lint.sh
│   ├── generate-component.sh
│   └── generate-service.sh
│
└── .github/
    ├── workflows/
    │   ├── ci.yml
    │   ├── cd.yml
    │   ├── test.yml
    │   └── deploy.yml
    ├── ISSUE_TEMPLATE/
    │   ├── bug_report.md
    │   ├── feature_request.md
    │   └── improvement.md
    └── PULL_REQUEST_TEMPLATE.md
```

---

*📁 Estructura completa del proyecto E-Commerce PYMES v1.0*
