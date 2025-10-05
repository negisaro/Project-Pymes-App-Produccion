import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  // Estilos legacy eliminados; se usa theme global auth
  styles: []
})

export class ForgotPasswordComponent {
  form: FormGroup;
  sent = false;
  errorMsg = '';
  loading = false;
  successMsg = '';
  currentYear = new Date().getFullYear();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.errorMsg = '';
    this.authService.sendResetPasswordEmail(this.form.value.email).subscribe({
      next: () => {
        this.sent = true;
        this.loading = false;
        this.form.reset();
        this.successMsg = '¡Correo enviado correctamente! Revisa tu bandeja de entrada.';
        this.toastr.success('Correo enviado correctamente', 'Recuperación de acceso');
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.message || 'No se pudo enviar el email';
        this.toastr.error(this.errorMsg, 'Error');
      },
    });
  }
}
