import { RoleName } from '../../../user/interfaces/user.interface';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { ThemeService, AppTheme } from '../../../core/services/theme.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  errorMsg: string = '';
  currentYear = new Date().getFullYear();
  theme: AppTheme = 'dark';

  constructor(
    private fb: FormBuilder,
    public authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private themeService: ThemeService
  ) {
    this.themeService.theme$.subscribe(t => this.theme = t);
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  private redirectByRole(): void {
    const user = this.authService.getCurrentUser();
    const roles = (user?.roles || []).map(r => r.name?.toUpperCase().replace(/^ROLE_/, ''));
    if (roles.some(r => r === 'ADMIN')) {
      this.router.navigate(['/admin']);
      return;
    }
    if (roles.some(r => ['CLIENT','CLIENTE','USER'].includes(r))) {
      this.router.navigate(['/cliente']);
      return;
    }
    this.router.navigate(['/']);
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.errorMsg = '';
    const { username, password } = this.loginForm.value;
    this.authService.login(username, password).subscribe({
      next: () => {
        this.loading = false;
        this.errorMsg = '';
        this.toastr.success('Bienvenido, acceso exitoso', 'Login');
        this.redirectByRole();
        this.loginForm.reset();
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.message || err || 'Error de autenticación';
        this.toastr.error(this.errorMsg, 'Error de autenticación');
      },
    });
  }

  toggleTheme(): void {
    this.themeService.toggle();
  }
}
