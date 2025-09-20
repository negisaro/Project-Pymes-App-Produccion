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
  private final CategoriaMapper categoriaMapper;

  public CategoriaServiceImpl(
    CategoriaRepository categoriaRepository,
    CategoriaMapper categoriaMapper
  ) {
    this.categoriaRepository = categoriaRepository;
    this.categoriaMapper = categoriaMapper;
  }

  @Override
  public CategoriaDTO update(Long id, CategoriaCreateDto categoriaCreateDto) {
    Categoria categoria = categoriaRepository
      .findById(id)
      .orElseThrow(() ->
        new IllegalArgumentException("Categoría no encontrada con id: " + id)
      );
    categoriaMapper.updateEntityFromDto(categoriaCreateDto, categoria);
    Categoria updated = categoriaRepository.save(categoria);
    return categoriaMapper.toDto(updated);
  }

  @Override
  public List<CategoriaDTO> findAll() {
    return categoriaRepository
      .findAll()
      .stream()
      .map(categoriaMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<CategoriaDTO> findById(Long id) {
    return categoriaRepository.findById(id).map(categoriaMapper::toDto);
  }

  @Override
  public Page<CategoriaDTO> findAll(Pageable pageable) {
    return categoriaRepository.findAll(pageable).map(categoriaMapper::toDto);
  }

  @Override
  public CategoriaDTO save(CategoriaCreateDto categoriaCreateDto) {
    Categoria categoria = categoriaMapper.fromCreateDto(categoriaCreateDto);
    Categoria saved = categoriaRepository.save(categoria);
    return categoriaMapper.toDto(saved);
  }

  @Override
  public void deleteById(Long id) {
    categoriaRepository.deleteById(id);
  }
}
