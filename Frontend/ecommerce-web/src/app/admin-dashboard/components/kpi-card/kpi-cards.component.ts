import { Component, Input } from '@angular/core';

@Component({
  selector: 'admin-kpi-cards',
  templateUrl: './kpi-cards.component.html',
  styleUrls: ['./kpi-cards.component.css']
})
export class KpiCardsComponent {
  @Input() kpis: any[] = [];
}
