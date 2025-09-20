import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProveedorService } from '../../service/proveedor.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-proveedor',
  templateUrl: './add-proveedor.component.html',
  styleUrls: ['./add-proveedor.component.css']
})
export class AddProveedorComponent implements OnInit {
  proveedorForm: FormGroup;
  esEdicion = false;
  idProveedor?: number;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private proveedorService: ProveedorService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.proveedorForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(255)]],
      descripcion: ['', [Validators.maxLength(1000)]],
      contacto: ['', [Validators.required, Validators.maxLength(255)]],
      activo: [true, Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.esEdicion = true;
        this.idProveedor = +id;
        this.loading = true;
        this.proveedorService.getProveedor(this.idProveedor).subscribe({
          next: (data) => {
            this.proveedorForm.patchValue({
              nombre: data.nombre,
              descripcion: data.descripcion,
              contacto: data.contacto,
              activo: data.activo
            });
            this.loading = false;
            this.showSwalToast('Proveedor cargado para edición', 'info');
          },
          error: () => {
            this.loading = false;
            this.showSwalError('No se pudo cargar el proveedor.');
            this.volver();
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.proveedorForm.invalid) {
      this.showSwalError('Por favor, completa todos los campos obligatorios y verifica los datos.');
      this.proveedorForm.markAllAsTouched();
      return;
    }
    const proveedor = this.proveedorForm.value;
    this.loading = true;
    if (this.esEdicion && this.idProveedor) {
      this.proveedorService.actualizarProveedor(this.idProveedor, proveedor).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Proveedor actualizado correctamente', 'success');
          this.volver();
        },
        error: () => {
          this.loading = false;
          this.showSwalError('No se pudo actualizar el proveedor.');
        }
      });
    } else {
      this.proveedorService.crearProveedor(proveedor).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Proveedor guardado correctamente', 'success');
          this.volver();
        },
        error: () => {
          this.loading = false;
          this.showSwalError('No se pudo guardar el proveedor.');
        }
      });
    }
  }

  onClear(): void {
    this.proveedorForm.reset({ activo: true });
    this.showSwalToast('El formulario ha sido limpiado.', 'info');
  }

  private showSwalToast(message: string, icon: 'success' | 'error' | 'info' | 'warning') {
    Swal.fire({
      toast: true,
      position: 'top-end',
      icon,
      title: message,
      showConfirmButton: false,
      timer: 2000,
      timerProgressBar: true
    });
  }

  private showSwalError(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#d33',
      timer: 2500
    });
  }

  volver(): void {
    this.router.navigate(['/dashboard/proveedor/list']);
  }
}
