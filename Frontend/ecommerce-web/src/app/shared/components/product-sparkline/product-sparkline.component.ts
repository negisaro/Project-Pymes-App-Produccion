import { Component, Input } from '@angular/core';

@Component({
  selector: 'product-sparkline',
  templateUrl: './product-sparkline.component.html',
  styleUrls: ['./product-sparkline.component.css']
})
export class ProductSparklineComponent {
  @Input() data: number[] = [];
  @Input() color: string = '#0d6efd';
  @Input() type: 'line' | 'bar' = 'line';
}
