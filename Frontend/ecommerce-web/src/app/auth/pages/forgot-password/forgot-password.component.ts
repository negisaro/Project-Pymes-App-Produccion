import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'], // <--- aquí
})

export class ForgotPasswordComponent {
  form: FormGroup;
  sent = false;
  errorMsg = '';
  loading = false;
  successMsg = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMsg = '';
    this.authService.sendResetPasswordEmail(this.form.value.email).subscribe({
      next: () => {
        this.sent = true;
        this.loading = false;
        this.form.reset();
        this.successMsg = '¡Correo enviado correctamente! Revisa tu bandeja de entrada.';
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.message || 'No se pudo enviar el email';
      },
    });
  }
}
