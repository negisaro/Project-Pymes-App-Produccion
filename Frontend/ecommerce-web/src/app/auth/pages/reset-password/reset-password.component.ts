import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styles: []
})

export class ResetPasswordComponent {
  form: FormGroup;
  success = false;
  errorMsg = '';
  loading = false;
  token: string;
  successMsg = '';
  currentYear = new Date().getFullYear();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      password: ['', Validators.required]
    });
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  submit() {
    if (this.form.invalid || !this.token) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.errorMsg = '';
    this.authService.resetPassword(this.token, this.form.value.password).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        this.form.reset();
        this.successMsg = '¡Contraseña cambiada correctamente! Ya puedes iniciar sesión.';
        this.toastr.success('Contraseña cambiada correctamente', 'Recuperación de acceso');
      },
      error: err => {
        this.loading = false;
        this.errorMsg = err?.message || 'No se pudo cambiar la contraseña';
        this.toastr.error(this.errorMsg, 'Error');
      }
    });
  }

  public goToLogin(): void {
    this.router.navigate(['/auth/login']);
  }
}
