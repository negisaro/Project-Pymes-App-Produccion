package com.nelson.project.msvc_categoria.msvc_categoria.service;

import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {
  List<Categoria> findAll();

  Optional<Categoria> findById(Long id);

  Page<Categoria> findAll(Pageable pageable);

  Categoria save(Categoria categoria);

  void deleteById(Long id);
}
