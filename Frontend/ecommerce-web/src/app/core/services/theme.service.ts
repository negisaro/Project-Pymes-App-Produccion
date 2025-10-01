import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type AppTheme = 'light' | 'dark';

const STORAGE_KEY = 'app-theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private _theme$ = new BehaviorSubject<AppTheme>('dark');
  public theme$ = this._theme$.asObservable();

  constructor() {
    const stored = (localStorage.getItem(STORAGE_KEY) as AppTheme) || null;
    if (stored === 'light' || stored === 'dark') {
      this.applyTheme(stored, false);
    } else {
      // Auto detectar preferencia del sistema
      const prefersLight = window.matchMedia('(prefers-color-scheme: light)').matches;
      this.applyTheme(prefersLight ? 'light' : 'dark', false);
    }
  }

  toggle(): void {
    const next: AppTheme = this._theme$.value === 'dark' ? 'light' : 'dark';
    this.applyTheme(next, true);
  }

  set(theme: AppTheme): void { this.applyTheme(theme, true); }

  private applyTheme(theme: AppTheme, persist: boolean) {
    this._theme$.next(theme);
    const root = document.documentElement;
    root.setAttribute('data-theme', theme);
    root.classList.add('theme-transition');
    window.setTimeout(() => root.classList.remove('theme-transition'), 300);
    if (persist) localStorage.setItem(STORAGE_KEY, theme);
  }
}
