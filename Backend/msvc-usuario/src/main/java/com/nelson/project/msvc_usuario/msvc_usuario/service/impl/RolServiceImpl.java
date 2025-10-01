package com.nelson.project.msvc_usuario.msvc_usuario.service.impl;

import com.nelson.project.msvc_usuario.msvc_usuario.exception.CustomException;
import com.nelson.project.msvc_usuario.msvc_usuario.exception.ErrorCodes;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación profesional y escalable del servicio de roles.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
@Transactional
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
  @Transactional
  public RolDto update(Long id, RolCreateDto rolCreateDto) {
    Rol rol = rolRepository
      .findById(id)
      .orElseThrow(() ->
        new CustomException(
          "Rol no encontrado con id: " + id,
          404,
          ErrorCodes.ROLE_NOT_FOUND
        )
      );
    rolMapper.updateEntityFromDto(rolCreateDto, rol);
    Rol updated = rolRepository.save(rol);
    return rolMapper.toDto(updated);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RolDto> findAll() {
    return rolRepository
      .findAll()
      .stream()
      .map(rolMapper::toDto)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RolDto> findById(Long id) {
    return rolRepository.findById(id).map(rolMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RolDto> findAll(Pageable pageable) {
    return rolRepository.findAll(pageable).map(rolMapper::toDto);
  }

  @Override
  @Transactional
  public RolDto save(RolCreateDto rolCreateDto) {
    Rol rol = rolMapper.fromCreateDto(rolCreateDto);
    Rol saved = rolRepository.save(rol);
    return rolMapper.toDto(saved);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    rolRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RolDto> findByName(String name) {
    logger.info("Buscando rol (DTO) por nombre: {}", name);
    return rolRepository.findByName(name).map(rolMapper::toDto);
  }
}
