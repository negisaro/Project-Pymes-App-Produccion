import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Proveedor } from '../../interfaces/proveedor';
import { ProveedorService } from '../../service/proveedor.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-proveedor',
  templateUrl: './add-proveedor.component.html',
  styleUrls: ['./add-proveedor.component.css']
})
export class AddProveedorComponent implements OnInit {
  proveedor: Proveedor = { nombre: '', descripcion: '', contacto: '', activo: true };
  esEdicion = false;
  idProveedor?: number;

  constructor(
    private proveedorService: ProveedorService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.esEdicion = true;
        this.idProveedor = +id;
        this.proveedorService.getProveedor(this.idProveedor).subscribe({
          next: (data) => this.proveedor = data,
          error: () => {
            Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo cargar el proveedor' });
            this.volver();
          }
        });
      }
    });
  }

  guardarProveedor(): void {
    if (this.esEdicion && this.idProveedor) {
      this.proveedorService.actualizarProveedor(this.idProveedor, this.proveedor).subscribe({
        next: () => {
          Swal.fire({ icon: 'success', title: 'Actualizado', text: 'Proveedor actualizado correctamente' })
            .then(() => this.volver());
        },
        error: () => Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo actualizar el proveedor' })
      });
    } else {
      this.proveedorService.crearProveedor(this.proveedor).subscribe({
        next: () => {
          Swal.fire({ icon: 'success', title: 'Guardado', text: 'Proveedor guardado correctamente' })
            .then(() => this.volver());
        },
        error: () => Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo guardar el proveedor' })
      });
    }
  }

  volver(): void {
    this.router.navigate(['/dashboard/proveedor/list']);
  }
}
