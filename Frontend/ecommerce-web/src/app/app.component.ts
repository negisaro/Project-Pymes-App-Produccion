
import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { UiLoaderService } from './shared/services/ui-loader.service';
import { BehaviorSubject, Subscription, timer, combineLatest } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  uiLoader = inject(UiLoaderService);
  private loadingSub?: Subscription;
  private safeLoading$ = new BehaviorSubject<boolean>(true);
  loading$ = this.safeLoading$.asObservable();

  ngOnInit() {
    // Combina el loading real con un timeout de seguridad (8s)
    this.loadingSub = combineLatest([
      this.uiLoader.loading$,
      timer(0, 1000)
    ]).subscribe(([realLoading, t]) => {
      if (!realLoading) {
        this.safeLoading$.next(false);
      } else if (t > 8) { // 8 segundos máximo
        this.safeLoading$.next(false);
      } else {
        this.safeLoading$.next(true);
      }
    });
  }

  ngOnDestroy() {
    this.loadingSub?.unsubscribe();
  }
}
