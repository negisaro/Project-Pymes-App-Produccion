import { ChangeDetectionStrategy, Component } from '@angular/core';

interface PedidoItem {
  id: number; cliente: string; fecha: Date; total: number; estado: string;
}

@Component({
  selector: 'app-list-pedido',
  templateUrl: './list-pedido.component.html',
  styleUrls: ['./list-pedido.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListPedidoComponent {
  pedidos: PedidoItem[] = Array.from({ length: 5 }).map((_, i) => ({
    id: 2000 + i,
    cliente: `Cliente ${i + 1}`,
    fecha: new Date(Date.now() - i * 86400000),
    total: Math.floor(Math.random() * 50000) + 5000,
    estado: ['PENDIENTE', 'COMPLETADO', 'CANCELADO'][i % 3]
  }));
}
