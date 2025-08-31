import { Component, effect, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['admin', Validators.required],
      password: ['admin123', Validators.required],
    });
  }

  errorMsg: string = '';

  private authEffect: any;

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
        // Esperar a que el AuthStatus sea 'authenticated' antes de navegar
        this.authEffect = effect(() => {
          if (this.authService.authStatus() === 'authenticated') {
            this.authEffect.destroy();
            this.router.navigateByUrl('/dashboard');
          }
        });
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.message || err || 'Error de autenticación';
        Swal.fire('Error', this.errorMsg, 'error');
      },
    });
  }
}
