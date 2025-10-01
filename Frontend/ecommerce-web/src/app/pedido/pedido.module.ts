import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PedidoRoutingModule } from './pedido-routing.module';
import { PedidoLayoutComponent } from './pedido-layout/pedido-layout.component';
import { ListPedidoComponent } from './pages/list-pedido/list-pedido.component';

@NgModule({
  declarations: [PedidoLayoutComponent, ListPedidoComponent],
  imports: [CommonModule, PedidoRoutingModule],
})
export class PedidoModule {}
