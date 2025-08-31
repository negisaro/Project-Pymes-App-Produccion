import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
  availableRoles: string[] = [];
  errorJson: any = null;
  debugResponse: any = null;
  debugError: any = null;

  ngOnInit() {
    // Obtener roles dinámicamente del backend
    this.authService.getRoles().subscribe({
      next: (roles: string[]) => {
        this.availableRoles = roles;
      },
      error: () => {
        this.availableRoles = ['USER', 'ADMIN', 'SUPERVISOR', 'EMPLEADO']; // fallback
      }
    });
  }

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      lastname: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(12)]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]],
      roles: [[], Validators.required],
      isActive: [true],
      admin: [false]
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
      roles: Array.isArray(formValue.roles)
        ? formValue.roles.map((rol: string) => ({ name: rol }))
        : [{ name: formValue.roles }],
      isActive: formValue.isActive,
      admin: formValue.admin
    };
    this.authService.register(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMsg = 'Usuario registrado correctamente. Redirigiendo al login...';
        this.form.reset();
        this.debugResponse = res;
        setTimeout(() => this.router.navigate(['/auth/login']), 1500);
      },
      error: err => {
        this.loading = false;
        this.error = err?.message || 'Error al registrar usuario';
        this.errorJson = err;
        this.debugError = err;
      }
    });
  }
}
