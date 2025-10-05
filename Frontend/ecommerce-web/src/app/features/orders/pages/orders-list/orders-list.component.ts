import { ChangeDetectionStrategy, Component } from '@angular/core';

interface Order {
  id: number;
  customer: string;
  date: Date;
  total: number;
  status: string;
}

@Component({
  selector: 'app-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrdersListComponent {
  orders: Order[] = Array.from({ length: 8 }).map((_, i) => ({
    id: 2000 + i,
    customer: `Cliente ${i + 1}`,
    date: new Date(Date.now() - i * 86400000),
    total: Math.floor(Math.random() * 50000) + 5000,
    status: ['PENDING', 'COMPLETED', 'CANCELLED'][i % 3]
  }));
}