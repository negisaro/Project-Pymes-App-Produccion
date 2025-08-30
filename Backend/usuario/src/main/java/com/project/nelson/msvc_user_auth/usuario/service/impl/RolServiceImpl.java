package com.project.nelson.msvc_user_auth.usuario.service.impl;

import com.project.nelson.msvc_user_auth.usuario.mapper.RolMapper;
import com.project.nelson.msvc_user_auth.usuario.model.dtos.RolDto;
import com.project.nelson.msvc_user_auth.usuario.model.entity.Rol;
import com.project.nelson.msvc_user_auth.usuario.repository.RolRepository;
import com.project.nelson.msvc_user_auth.usuario.service.RolService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación profesional y escalable del servicio de roles.
 * Incluye manejo de transacciones, logging, uso de mappers y buenas prácticas.
 */
@Service
public class RolServiceImpl implements RolService {

    private static final Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        logger.info("Buscando todos los roles");
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDto> findAllDto() {
        logger.info("Buscando todos los roles (DTO)");
        return rolRepository.findAll().stream()
            .map(rolMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findById(Long id) {
        logger.info("Buscando rol por id: {}", id);
        return rolRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDto> findDtoById(Long id) {
        logger.info("Buscando rol (DTO) por id: {}", id);
        return rolRepository.findById(id).map(rolMapper::toDto);
    }

    @Override
    @Transactional
    public Rol save(Rol rol) {
        logger.info("Guardando rol: {}", rol.getName());
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public RolDto saveDto(RolDto rolDto) {
        logger.info("Guardando rol (DTO): {}", rolDto.getName());
        Rol rol = rolMapper.toEntity(rolDto);
        Rol saved = save(rol);
        return rolMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.warn("Eliminando rol por id: {}", id);
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByName(String name) {
        logger.info("Buscando rol por nombre: {}", name);
        return rolRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDto> findDtoByName(String name) {
        logger.info("Buscando rol (DTO) por nombre: {}", name);
        return rolRepository.findByName(name).map(rolMapper::toDto);
    }
}