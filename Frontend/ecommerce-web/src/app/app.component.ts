
import { Component, inject } from '@angular/core';
import { UiLoaderService } from './shared/services/ui-loader.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  uiLoader = inject(UiLoaderService);
  loading$ = this.uiLoader.loading$;
}
