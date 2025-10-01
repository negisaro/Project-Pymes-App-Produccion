import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PedidoLayoutComponent } from './pedido-layout/pedido-layout.component';
import { ListPedidoComponent } from './pages/list-pedido/list-pedido.component';

const routes: Routes = [
  {
    path: '',
    component: PedidoLayoutComponent,
    children: [
  { path: '', component: ListPedidoComponent },
      // Futuras rutas: detalle, edición, etc.
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PedidoRoutingModule {}
