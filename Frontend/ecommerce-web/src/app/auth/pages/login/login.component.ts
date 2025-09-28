import { RoleName } from '../../../user/interfaces/user.interface';
import { Component, effect, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  errorMsg: string = '';

  constructor(
    private fb: FormBuilder,
    public authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  // Declarar effect como propiedad de clase
  public loginEffect = effect(() => {
    if (this.authService.authStatus() === 'authenticated') {
      this.toastr.success('Bienvenido, acceso exitoso', 'Login');
      // Obtener roles del usuario autenticado
      const user = this.authService.getCurrentUser();
      const roles = user?.roles?.map(r => r.name) || [];
      if (roles.includes(RoleName.ADMIN)) {
        this.router.navigateByUrl('/dashboard-admin');
      } else if (roles.includes(RoleName.USER) || roles.includes(RoleName.CLIENTE)) {
        this.router.navigateByUrl('/dashboard-cliente');
      } else {
        this.router.navigateByUrl('/dashboard'); // fallback
      }
    }
  });

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;
    this.loading = true;
    this.errorMsg = '';
    const { username, password } = this.loginForm.value;
    this.authService.login(username, password).subscribe({
      next: () => {
        this.loginForm.reset();
        this.loading = false;
        this.errorMsg = '';
        // La navegación y feedback de éxito se realiza automáticamente por el effect
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.message || err || 'Error de autenticación';
        this.toastr.error(this.errorMsg, 'Error de autenticación');
      },
    });
  }
}
