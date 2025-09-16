package com.nelson.project.msvc_proveedor.msvc_proveedor.service;

import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorCreateDTO;
import com.nelson.project.msvc_proveedor.msvc_proveedor.model.dto.ProveedorDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para la gestión de proveedores.
 * Define las operaciones de negocio para el manejo de proveedores en el sistema.
 */
public interface ProveedorService {
  /**
   * Obtiene la lista de todos los proveedores registrados.
   * @return lista de ProveedorDTO
   */
  List<ProveedorDTO> findAll();

  /**
   * Busca un proveedor por su identificador único.
   * @param id identificador del proveedor
   * @return Optional con ProveedorDTO si existe
   */
  Optional<ProveedorDTO> findById(Long id);

  /**
   * Registra un nuevo proveedor en el sistema.
   * @param proveedorCreateDto datos del proveedor a crear
   * @return ProveedorDTO creado
   */
  ProveedorDTO save(ProveedorCreateDTO proveedorCreateDto);

  /**
   * Elimina un proveedor por su identificador único.
   * @param id identificador del proveedor a eliminar
   */
  void deleteById(Long id);

  /**
   * Obtiene la lista de proveedores activos.
   * @return lista de ProveedorDTO activos
   */
  List<ProveedorDTO> findActivos();

  /**
   * Busca proveedores cuyo nombre contenga el texto dado (ignorando mayúsculas/minúsculas).
   * @param nombre texto a buscar en el nombre
   * @return lista de ProveedorDTO coincidentes
   */
  List<ProveedorDTO> findByNombre(String nombre);

  /**
   * Busca proveedores asociados a un producto específico (integración con microservicio producto).
   * @param productoId identificador del producto
   * @return lista de ProveedorDTO relacionados
   */
  List<ProveedorDTO> findByProductoId(Long productoId);

  /**
   * Busca proveedores por una lista de identificadores.
   * @param ids lista de IDs de proveedores
   * @return lista de ProveedorDTO encontrados
   */
  List<ProveedorDTO> findByIds(List<Long> ids);

  /**
   * Obtiene la lista paginada de proveedores.
   * @param pageable información de paginación y orden
   * @return página de ProveedorDTO
   */
  Page<ProveedorDTO> findAll(Pageable pageable);

  /**
   * Actualiza un proveedor existente por su ID.
   * @param id identificador del proveedor a actualizar
   * @param proveedorCreateDto datos nuevos del proveedor
   * @return ProveedorDTO actualizado
   */
  ProveedorDTO update(Long id, ProveedorCreateDTO proveedorCreateDto);
}
