import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  form: FormGroup;
  error: string = '';
  loading = false;
  successMsg: string = '';
  // availableRoles: string[] = [];
  errorJson: any = null;
  debugResponse: any = null;
  debugError: any = null;

  ngOnInit() {
    // Ya no se requiere cargar roles, el backend asigna uno por defecto
  }

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      lastname: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(12)]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]],
      isActive: [true]
    });
  }

  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';
    const hasUpperCase = /[A-Z]/.test(value);
    const hasNumber = /[0-9]/.test(value);
    if (!hasUpperCase || !hasNumber) {
      return { passwordStrength: true };
    }
    return null;
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    this.successMsg = '';
    this.errorJson = null;
    this.debugResponse = null;
    this.debugError = null;
    // Adaptar el objeto para enviar solo los campos requeridos por el backend
    const formValue = this.form.value;
    const payload: any = {
      name: formValue.name,
      lastname: formValue.lastname,
      email: formValue.email,
      username: formValue.username,
      password: formValue.password,
      active: formValue.isActive
    };
    this.authService.register(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMsg = 'Usuario registrado correctamente. Redirigiendo al login...';
        this.toastr.success('Usuario registrado correctamente', 'Registro exitoso');
        this.form.reset({ isActive: true });
        this.debugResponse = res;
        setTimeout(() => this.router.navigate(['/dashboard/auth/login']), 1500);
      },
      error: err => {
        this.loading = false;
        const backendMsg = err?.error?.mensaje || err?.message || 'Error al registrar usuario';
        this.error = backendMsg;
        this.toastr.error(backendMsg, 'Error');
        this.errorJson = err;
        this.debugError = err;
      }
    });
  }
}
