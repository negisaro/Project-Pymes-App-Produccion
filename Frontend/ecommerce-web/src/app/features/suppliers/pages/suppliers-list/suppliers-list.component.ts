import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Supplier } from '../../interfaces/supplier.interface';
import { SupplierService } from '../../services/supplier.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-suppliers-list',
  templateUrl: './suppliers-list.component.html',
  styleUrls: ['./suppliers-list.component.css']
})
export class SuppliersListComponent implements OnInit {
  suppliers: Supplier[] = [];
  message: string | null = null;
  errors: any[] = [];
  page = 0;
  size = 8;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private supplierService: SupplierService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.message = null;
    this.errors = [];
    this.supplierService.getSuppliers(this.page, this.size).subscribe({
      next: (data: any) => {
        this.suppliers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: () => {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error loading suppliers' });
      }
    });
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadSuppliers();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadSuppliers();
    }
  }

  editSupplier(supplier: Supplier): void {
    this.router.navigate(['edit', supplier.id], { relativeTo: this.route });
  }

  deleteSupplier(id?: number): void {
    if (!id) return;
    Swal.fire({
      title: 'Are you sure you want to delete this supplier?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete',
      cancelButtonText: 'Cancel'
    }).then(result => {
      if (result.isConfirmed) {
        this.supplierService.deleteSupplier(id).subscribe({
          next: () => {
            this.showSwalToast('Supplier deleted successfully', 'success');
            this.loadSuppliers();
          },
          error: () => {
            this.showSwalToast('Error deleting supplier', 'error');
          }
        });
      }
    });
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

  reload(): void {
    this.loadSuppliers();
  }

  addSupplier() {
    this.router.navigate(['add'], { relativeTo: this.route });
  }
}