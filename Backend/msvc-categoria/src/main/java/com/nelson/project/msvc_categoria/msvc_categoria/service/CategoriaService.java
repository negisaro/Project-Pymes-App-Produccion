package com.nelson.project.msvc_categoria.msvc_categoria.service;

import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de servicio para gestión de categorías.
 * Aplica buenas prácticas modernas y principios SOLID.
 */
public interface CategoriaService {
  /**
   * Obtiene todas las categorías.
   */
  List<CategoriaDTO> findAll();

  /**
   * Busca una categoría por su ID.
   */
  Optional<CategoriaDTO> findById(Long id);

  /**
   * Obtiene categorías paginadas.
   */
  Page<CategoriaDTO> findAll(Pageable pageable);

  /**
   * Crea una nueva categoría.
   */
  CategoriaDTO save(CategoriaCreateDto categoriaCreateDto);

  /**
   * Actualiza una categoría existente por su ID.
   */
  CategoriaDTO update(Long id, CategoriaCreateDto categoriaCreateDto);

  /**
   * Elimina una categoría por su ID.
   */
  void deleteById(Long id);
}
