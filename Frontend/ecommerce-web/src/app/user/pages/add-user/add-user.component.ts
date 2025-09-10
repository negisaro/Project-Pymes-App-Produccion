import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { RoleName, User } from '../../interfaces/user.interface';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'user-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css'],
})
export class AddUserComponent implements OnInit {
  userForm!: FormGroup;
  rolesList = [
    { value: RoleName.ADMIN, label: 'Administrador' },
    { value: RoleName.USER, label: 'Usuario' },
    { value: RoleName.SUPERVISOR, label: 'Supervisor' },
    { value: RoleName.EMPLEADO, label: 'Empleado' },
  ];
  isEditMode = false;
  userId?: number;
  loading = false;

  // Debug visual
  debugResponse: any = null;
  debugError: any = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      lastname: ['', [Validators.required, Validators.maxLength(100)]],
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(12)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]], // Cambia aquí
      password: [''],
      roles: [[], [Validators.required]],
      active: [true]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.userId = +id;
        this.loadUser(+id);
  // En edición, el password no es obligatorio ni visible
  this.userForm.get('password')?.clearValidators();
  this.userForm.get('password')?.setValue('');
  this.userForm.get('password')?.updateValueAndValidity();
      }
    });
  }

  private loadUser(id: number) {
    this.loading = true;
    this.userService.getUser().subscribe({
      next: (users) => {
        const user = users.find(u => u.id === id);
        if (user) {
          this.userForm.patchValue({
            name: user.name,
            lastname: user.lastname,
            username: user.username,
            email: user.email, // ← Corrección aquí
            roles: user.roles,
            active: user.active
          });
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        Swal.fire('Error', 'No se pudo cargar el usuario.', 'error');
      }
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      Swal.fire({
        icon: 'error',
        title: 'Formulario incompleto',
        text: 'Por favor, completa todos los campos obligatorios y verifica los datos.',
        confirmButtonText: 'Entendido',
      });
      this.userForm.markAllAsTouched();
      return;
    }
    if (this.isEditMode && this.userId) {
      const user: User = {
        ...this.userForm.value,
        id: this.userId,
        password: '' // No se actualiza la contraseña aquí
      };
      this.userService.updateUser(user).subscribe({
        next: (resp) => {
          this.debugResponse = resp;
          this.debugError = null;
          Swal.fire({
            icon: 'success',
            title: 'Usuario actualizado',
            text: 'El usuario ha sido actualizado exitosamente.',
            timer: 1500,
            showConfirmButton: false,
          });
          this.router.navigate(['/dashboard/user/list-user']);
        },
        error: (err) => {
          this.debugError = err;
          this.debugResponse = null;
          Swal.fire('Error', 'No se pudo actualizar el usuario.', 'error');
        }
      });
    } else {
      const user: User = this.userForm.value;
      this.userService.addUser(user).subscribe({
        next: (resp) => {
          this.debugResponse = resp;
          this.debugError = null;
          Swal.fire({
            icon: 'success',
            title: 'Usuario creado',
            text: 'El usuario ha sido registrado exitosamente.',
            timer: 1500,
            showConfirmButton: false,
          });
          this.userForm.reset({ active: true, roles: [] });
          this.router.navigate(['/dashboard/user/list-user']);
        },
        error: (err) => {
          this.debugError = err;
          this.debugResponse = null;
          Swal.fire('Error', 'No se pudo registrar el usuario.', 'error');
        }
      });
    }
  }

  onClear(): void {
    if (this.isEditMode) {
      this.loadUser(this.userId!);
    } else {
      this.userForm.reset({ active: true, roles: [] });
    }
    Swal.fire({
      icon: 'info',
      title: 'Formulario limpio',
      text: 'El formulario ha sido limpiado.',
      timer: 1500,
      showConfirmButton: false,
    });
  }
}
