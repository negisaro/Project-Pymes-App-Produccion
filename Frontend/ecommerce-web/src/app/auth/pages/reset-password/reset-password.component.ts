import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})

export class ResetPasswordComponent {
  form: FormGroup;
  success = false;
  errorMsg = '';
  loading = false;
  token: string;
  successMsg = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      password: ['', Validators.required]
    });
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  submit() {
    if (this.form.invalid || !this.token) return;
    this.loading = true;
    this.errorMsg = '';
    this.authService.resetPassword(this.token, this.form.value.password).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        this.form.reset();
        this.successMsg = '¡Contraseña cambiada correctamente! Ya puedes iniciar sesión.';
      },
      error: err => {
        this.loading = false;
        this.errorMsg = err?.message || 'No se pudo cambiar la contraseña';
      }
    });
  }

  public goToLogin(): void {
    this.router.navigate(['/auth/login']);
  }
}
