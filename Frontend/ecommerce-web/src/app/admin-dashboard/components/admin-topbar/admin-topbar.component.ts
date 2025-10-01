import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-admin-topbar',
  templateUrl: './admin-topbar.component.html',
  styleUrls: ['./admin-topbar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminTopbarComponent {
  userName = 'Admin';
}
