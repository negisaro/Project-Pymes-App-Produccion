import { Component, Input } from '@angular/core';

@Component({
  selector: 'admin-categorias-pie-chart',
  templateUrl: './categorias-pie-chart.component.html',
  styleUrls: ['./categorias-pie-chart.component.css']
})
export class CategoriasPieChartComponent {
  @Input() series: any[] = [];
  @Input() labels: string[] = [];
  @Input() colors: string[] = [];
}
