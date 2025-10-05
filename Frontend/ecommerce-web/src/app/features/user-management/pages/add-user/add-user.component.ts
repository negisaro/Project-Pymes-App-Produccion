import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';


import { Role, RoleName, User } from '../../../../core/models/user.model';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environments';

// DTO para crear/actualizar usuario
interface UserCreateDto {
  name: string;
  lastname: string;
  username: string;
  password?: string;
  email: string;
  rolesIds: number[];
}

@Component({
  selector: 'user-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css'],
})
export class AddUserComponent implements OnInit {
  userForm!: FormGroup;
  rolesList: Role[] = [];
  isEditMode = false;
  userId?: number;
  loading = false;

  // Debug visual


  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      lastname: ['', [Validators.required, Validators.maxLength(100)]],
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(12),
        ],
      ],
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(100)],
      ],
      password: [''],
      roles: [[], [Validators.required]],
      active: [true],
    });

    // Cargar roles dinámicamente desde el backend
    this.http.get<Role[]>(`${environment.baseUrl}/api/segura/roles/list`).subscribe({
      next: (roles) => {
        console.log('[DEBUG] Roles recibidos del backend:', roles);
        this.rolesList = roles;
      },
      error: (err) => {
        console.error('[ERROR] No se pudieron obtener los roles:', err);
        this.rolesList = [];
      },
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.userId = +id;
        this.loadUserById(+id);
        // En edición, el password no es obligatorio ni visible
        this.userForm.get('password')?.clearValidators();
        this.userForm.get('password')?.setValue('');
        this.userForm.get('password')?.updateValueAndValidity();
      }
    });
  }

  private loadUserById(id: number) {
    this.loading = true;
    this.userService.getUserById(id).subscribe({
      next: (user) => {
        // Mapear los roles del usuario a los objetos Role de rolesList
        const userRoleIds = user.roles.map((r) => r.id);
        const selectedRoles = this.rolesList.filter((role) => userRoleIds.includes(role.id));
        this.userForm.patchValue({
          name: user.name,
          lastname: user.lastname,
          username: user.username,
          email: user.email,
          roles: selectedRoles,
          active: user.active,
        });
        this.loading = false;
        this.showSwalToast('Usuario cargado para edición', 'info');
      },
      error: () => {
        this.loading = false;
        this.showSwalError('No se pudo cargar el usuario.');
        this.router.navigate(['../list-user'], { relativeTo: this.route });
      },
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.showSwalError(
        'Por favor, completa todos los campos obligatorios y verifica los datos.'
      );
      this.userForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    const formValue = this.userForm.value;
    const userDto: UserCreateDto = {
      name: formValue.name,
      lastname: formValue.lastname,
      username: formValue.username,
      password: formValue.password || undefined,
      email: formValue.email,
      rolesIds: Array.isArray(formValue.roles)
        ? formValue.roles.map((r: Role) => r.id)
        : [],
    };

    if (this.isEditMode && this.userId) {
      this.userService.updateUser(this.userId, userDto).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Usuario actualizado exitosamente', 'success');
    this.router.navigate(['../list-user'], { relativeTo: this.route });
        },
        error: (err) => {
          this.loading = false;
          const backendMsg =
            err?.error?.mensaje || 'No se pudo actualizar el usuario.';
          this.showSwalError(backendMsg);
        },
      });
    } else {
      this.userService.addUser(userDto).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Usuario registrado exitosamente', 'success');
          this.userForm.reset({ active: true, roles: [] });
    this.router.navigate(['../list-user'], { relativeTo: this.route });
        },
        error: (err) => {
          this.loading = false;
          const backendMsg =
            err?.error?.mensaje || 'No se pudo registrar el usuario.';
          this.showSwalError(backendMsg);
        },
      });
    }
  }

  onClear(): void {
    if (this.isEditMode) {
      this.loadUserById(this.userId!);
    } else {
      this.userForm.reset({ active: true, roles: [] });
    }
    this.showSwalToast('El formulario ha sido limpiado.', 'info');
  }

  private showSwalToast(
    message: string,
    icon: 'success' | 'error' | 'info' | 'warning'
  ) {
    Swal.fire({
      toast: true,
      position: 'top-end',
      icon,
      title: message,
      showConfirmButton: false,
      timer: 2000,
      timerProgressBar: true,
    });
  }

  private showSwalError(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#d33',
      timer: 2500,
    });
  }
}
