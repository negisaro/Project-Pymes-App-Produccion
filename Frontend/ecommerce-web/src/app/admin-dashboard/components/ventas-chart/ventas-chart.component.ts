import { Component, Input } from '@angular/core';

@Component({
  selector: 'admin-ventas-chart',
  templateUrl: './ventas-chart.component.html',
  styleUrls: ['./ventas-chart.component.css']
})
export class VentasChartComponent {
  @Input() series: any[] = [];
  @Input() chart: any;
  @Input() xaxis: any;
  @Input() colors: string[] = [];
}
