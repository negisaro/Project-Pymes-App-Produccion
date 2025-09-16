package com.nelson.project.msvc_categoria.msvc_categoria.service.impl;

import com.nelson.project.msvc_categoria.msvc_categoria.mapper.CategoriaMapper;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaCreateDto;
import com.nelson.project.msvc_categoria.msvc_categoria.model.dto.CategoriaDTO;
import com.nelson.project.msvc_categoria.msvc_categoria.model.entity.Categoria;
import com.nelson.project.msvc_categoria.msvc_categoria.repository.CategoriaRepository;
import com.nelson.project.msvc_categoria.msvc_categoria.service.CategoriaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService {

  private final CategoriaRepository categoriaRepository;

  public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
    this.categoriaRepository = categoriaRepository;
  }

  @Override
  public CategoriaDTO update(Long id, CategoriaCreateDto categoriaCreateDto) {
    Categoria categoria = categoriaRepository
      .findById(id)
      .orElseThrow(() ->
        new IllegalArgumentException("Categoría no encontrada con id: " + id)
      );
    CategoriaMapper.updateEntityFromDto(categoria, categoriaCreateDto);
    Categoria updated = categoriaRepository.save(categoria);
    return CategoriaMapper.toDto(updated);
  }

  @Override
  public List<CategoriaDTO> findAll() {
    return categoriaRepository
      .findAll()
      .stream()
      .map(CategoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<CategoriaDTO> findById(Long id) {
    return categoriaRepository.findById(id).map(CategoriaMapper::toDto);
  }

  @Override
  public Page<CategoriaDTO> findAll(Pageable pageable) {
    return categoriaRepository.findAll(pageable).map(CategoriaMapper::toDto);
  }

  @Override
  public CategoriaDTO save(CategoriaCreateDto categoriaCreateDto) {
    Categoria categoria = CategoriaMapper.fromCreateDto(categoriaCreateDto);
    Categoria saved = categoriaRepository.save(categoria);
    return CategoriaMapper.toDto(saved);
  }

  @Override
  public void deleteById(Long id) {
    categoriaRepository.deleteById(id);
  }
}
