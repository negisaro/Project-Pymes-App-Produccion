package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.mapper.RolMapper;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.RolDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Rol;
import com.nelson.project.msvc_usuario.msvc_usuario.repository.RolRepository;
import com.nelson.project.msvc_usuario.msvc_usuario.service.RolService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementación profesional y escalable del servicio de roles.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
public class RolServiceImpl implements RolService {

  private static final Logger logger = LoggerFactory.getLogger(
    RolServiceImpl.class
  );

  private final RolRepository rolRepository;
  private final RolMapper rolMapper;

  public RolServiceImpl(RolRepository rolRepository, RolMapper rolMapper) {
    this.rolRepository = rolRepository;
    this.rolMapper = rolMapper;
  }

  @Override
  public RolDto update(Long id, RolCreateDto rolCreateDto) {
    Rol rol = rolRepository
      .findById(id)
      .orElseThrow(() ->
        new IllegalArgumentException("Rol no encontrado con id: " + id)
      );
    rolMapper.updateEntityFromDto(rolCreateDto, rol);
    Rol updated = rolRepository.save(rol);
    return rolMapper.toDto(updated);
  }

  @Override
  public List<RolDto> findAll() {
    return rolRepository
      .findAll()
      .stream()
      .map(rolMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<RolDto> findById(Long id) {
    return rolRepository.findById(id).map(rolMapper::toDto);
  }

  @Override
  public Page<RolDto> findAll(Pageable pageable) {
    return rolRepository.findAll(pageable).map(rolMapper::toDto);
  }

  @Override
  public RolDto save(RolCreateDto rolCreateDto) {
    Rol rol = rolMapper.fromCreateDto(rolCreateDto);
    Rol saved = rolRepository.save(rol);
    return rolMapper.toDto(saved);
  }

  @Override
  public void deleteById(Long id) {
    rolRepository.deleteById(id);
  }

  @Override
  public Optional<RolDto> findByName(String name) {
    logger.info("Buscando rol (DTO) por nombre: {}", name);
    return rolRepository.findByName(name).map(rolMapper::toDto);
  }
}
