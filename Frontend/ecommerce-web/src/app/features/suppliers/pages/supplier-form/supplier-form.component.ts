import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from '../../services/supplier.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-supplier-form',
  templateUrl: './supplier-form.component.html',
  styleUrls: ['./supplier-form.component.css']
})
export class SupplierFormComponent implements OnInit {
  supplierForm: FormGroup;
  isEditing = false;
  supplierId?: number;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private supplierService: SupplierService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.supplierForm = this.fb.group({
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
        this.isEditing = true;
        this.supplierId = +id;
        this.loading = true;
        this.supplierService.getSupplier(this.supplierId).subscribe({
          next: (data) => {
            this.supplierForm.patchValue({
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
            this.goBack();
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.supplierForm.invalid) {
      this.showSwalError('Por favor complete todos los campos requeridos y verifique los datos.');
      this.supplierForm.markAllAsTouched();
      return;
    }
    const supplier = this.supplierForm.value;
    this.loading = true;
    if (this.isEditing && this.supplierId) {
      this.supplierService.updateSupplier(this.supplierId, supplier).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Proveedor actualizado exitosamente', 'success');
          this.goBack();
        },
        error: () => {
          this.loading = false;
          this.showSwalError('No se pudo actualizar el proveedor.');
        }
      });
    } else {
      this.supplierService.createSupplier(supplier).subscribe({
        next: () => {
          this.loading = false;
          this.showSwalToast('Proveedor guardado exitosamente', 'success');
          this.goBack();
        },
        error: () => {
          this.loading = false;
          this.showSwalError('No se pudo guardar el proveedor.');
        }
      });
    }
  }

  onClear(): void {
    this.supplierForm.reset({ activo: true });
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

  goBack(): void {
    this.router.navigate(['/admin/dashboard-admin/suppliers']);
  }
}
