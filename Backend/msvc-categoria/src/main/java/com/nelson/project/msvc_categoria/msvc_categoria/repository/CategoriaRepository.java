package com.nelson.project.msvc_categoria.msvc_categoria.repository;

import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  @SuppressWarnings("null")
  Page<Categoria> findAll(Pageable pageable);
}
