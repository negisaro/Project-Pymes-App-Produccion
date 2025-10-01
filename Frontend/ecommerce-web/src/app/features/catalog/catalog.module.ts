import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CatalogRoutingModule } from './catalog.routing';
import { ProductTileComponent } from './components/product-tile/product-tile.component';
import { CatalogPageComponent } from './pages/catalog-page/catalog-page.component';

@NgModule({
  declarations: [ProductTileComponent, CatalogPageComponent],
  imports: [CommonModule, RouterModule, CatalogRoutingModule],
})
export class CatalogModule {}
