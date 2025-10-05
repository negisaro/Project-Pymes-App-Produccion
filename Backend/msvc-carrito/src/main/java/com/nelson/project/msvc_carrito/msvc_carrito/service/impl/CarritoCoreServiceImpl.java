package com.nelson.project.msvc_carrito.msvc_carrito.service.impl;

// import com.nelson.project.msvc_carrito.msvc_carrito.clientfeign.UsuarioFeignClient;
import com.nelson.project.msvc_carrito.msvc_carrito.event.CarritoAbandonadoEvent;
import com.nelson.project.msvc_carrito.msvc_carrito.event.CarritoCreadoEvent;
import com.nelson.project.msvc_carrito.msvc_carrito.exception.BusinessException;
import com.nelson.project.msvc_carrito.msvc_carrito.mapper.CarritoMapper;
import com.nelson.project.msvc_carrito.msvc_carrito.model.dto.CarritoDto;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.Carrito;
import com.nelson.project.msvc_carrito.msvc_carrito.model.entity.EstadoCarrito;
// import com.nelson.project.msvc_carrito.msvc_carrito.repository.CarritoHistorialRepository;
import com.nelson.project.msvc_carrito.msvc_carrito.repository.CarritoRepository;
import com.nelson.project.msvc_carrito.msvc_carrito.service.CarritoCoreService;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio core de carrito.
 *
 * @author Sistema Automatizado
 * @version 1.0.0
 * @since 2025-10-01
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarritoCoreServiceImpl implements CarritoCoreService {

  private static final Logger log = LoggerFactory.getLogger(
    CarritoCoreServiceImpl.class
  );

  private static final int HORAS_EXPIRACION_CARRITO = 72;
  private static final String CACHE_CARRITO = "carritos";

  private final CarritoRepository carritoRepository;
  // TODO: Implementar funcionalidad de historial
  // private final CarritoHistorialRepository carritoHistorialRepository;
  private final CarritoMapper carritoMapper;
  // TODO: Implementar validación de usuarios existentes
  // private final UsuarioFeignClient usuarioClient;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Cacheable(
    value = CACHE_CARRITO,
    key = "#usuarioId",
    unless = "#result == null"
  )
  @Transactional(readOnly = true, timeout = 10)
  public CarritoDto obtenerCarritoPorUsuario(Long usuarioId) {
    log.info("Obteniendo carrito para usuario: {}", usuarioId);

    try {
      validarExistenciaUsuario(usuarioId);

      Optional<Carrito> carritoOpt =
        carritoRepository.findCarritoActivoPorUsuario(usuarioId);

      if (carritoOpt.isEmpty()) {
        log.info(
          "No existe carrito activo para usuario {}, creando nuevo carrito",
          usuarioId
        );
        return crearCarrito(usuarioId, obtenerIpCliente());
      }

      Carrito carrito = carritoOpt.get();

      if (carritoHaExpirado(carrito.getId())) {
        log.info(
          "Carrito {} ha expirado, marcando como abandonado",
          carrito.getId()
        );
        marcarCarritoComoAbandonado(carrito.getId());
        return crearCarrito(usuarioId, obtenerIpCliente());
      }

      actualizarUltimaActividad(carrito.getId());

      CarritoDto carritoDto = carritoMapper.toDto(carrito);

      log.info(
        "Carrito obtenido exitosamente para usuario {}: {} items, total: {}",
        usuarioId,
        carrito.getTotalItems(),
        carrito.getTotal()
      );

      return carritoDto;
    } catch (Exception e) {
      log.error(
        "Error obteniendo carrito para usuario {}: {}",
        usuarioId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error obteniendo carrito del usuario",
        "CARRITO_OBTENER_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CarritoDto> obtenerCarritoPorId(
    Long carritoId,
    Long usuarioId
  ) {
    log.info("Obteniendo carrito {} para usuario {}", carritoId, usuarioId);

    try {
      Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);

      if (carritoOpt.isEmpty()) {
        return Optional.empty();
      }

      Carrito carrito = carritoOpt.get();

      if (!Objects.equals(carrito.getUsuarioId(), usuarioId)) {
        log.warn(
          "Usuario {} intentó acceder al carrito {} que no le pertenece",
          usuarioId,
          carritoId
        );
        throw new BusinessException(
          "No tiene permisos para acceder a este carrito",
          "ACCESO_DENEGADO"
        );
      }

      CarritoDto carritoDto = carritoMapper.toDto(carrito);
      return Optional.of(carritoDto);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error obteniendo carrito {} para usuario {}: {}",
        carritoId,
        usuarioId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error obteniendo carrito por ID",
        "CARRITO_OBTENER_ID_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional(
    propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED
  )
  @CacheEvict(value = CACHE_CARRITO, key = "#usuarioId")
  public CarritoDto crearCarrito(Long usuarioId, String ipCliente) {
    log.info("Creando nuevo carrito para usuario: {}", usuarioId);

    try {
      validarExistenciaUsuario(usuarioId);

      Optional<Carrito> carritoExistente =
        carritoRepository.findCarritoActivoPorUsuario(usuarioId);
      if (carritoExistente.isPresent()) {
        log.warn(
          "Usuario {} ya tiene un carrito activo: {}",
          usuarioId,
          carritoExistente.get().getId()
        );
        return carritoMapper.toDto(carritoExistente.get());
      }

      Carrito nuevoCarrito = new Carrito();
      nuevoCarrito.setUsuarioId(usuarioId);
      nuevoCarrito.setEstado(EstadoCarrito.ACTIVO);
      nuevoCarrito.setIpCliente(ipCliente);
      nuevoCarrito.setExpiraEn(
        LocalDateTime.now().plusHours(HORAS_EXPIRACION_CARRITO)
      );

      nuevoCarrito = carritoRepository.save(nuevoCarrito);

      // Publicar evento de carrito creado
      eventPublisher.publishEvent(
        new CarritoCreadoEvent(this, nuevoCarrito.getId(), usuarioId, ipCliente)
      );

      CarritoDto carritoDto = carritoMapper.toDto(nuevoCarrito);

      log.info(
        "Carrito creado exitosamente para usuario {}: ID {}",
        usuarioId,
        nuevoCarrito.getId()
      );

      return carritoDto;
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error creando carrito para usuario {}: {}",
        usuarioId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error creando carrito",
        "CARRITO_CREAR_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional
  public void marcarCarritoComoAbandonado(Long carritoId) {
    log.info("Marcando carrito {} como abandonado", carritoId);

    try {
      Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
      if (carritoOpt.isPresent()) {
        Carrito carrito = carritoOpt.get();
        carrito.setEstado(EstadoCarrito.ABANDONADO);
        carrito.setFechaAbandonado(LocalDateTime.now());
        carritoRepository.save(carrito);

        // Publicar evento de carrito abandonado
        eventPublisher.publishEvent(
          new CarritoAbandonadoEvent(
            this,
            carritoId,
            carrito.getUsuarioId(),
            carrito.getTotalItems(),
            carrito.getTotal().toString()
          )
        );

        log.info("Carrito {} marcado como abandonado exitosamente", carritoId);
      }
    } catch (Exception e) {
      log.error(
        "Error marcando carrito {} como abandonado: {}",
        carritoId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error marcando carrito como abandonado",
        "CARRITO_ABANDONO_ERROR",
        e
      );
    }
  }

  @Override
  @Transactional
  public void actualizarUltimaActividad(Long carritoId) {
    try {
      Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
      if (carritoOpt.isPresent()) {
        Carrito carrito = carritoOpt.get();
        carrito.setUltimaActividad(LocalDateTime.now());
        carritoRepository.save(carrito);
      }
    } catch (Exception e) {
      log.warn(
        "Error actualizando última actividad del carrito {}: {}",
        carritoId,
        e.getMessage()
      );
    }
  }

  @Override
  public boolean carritoHaExpirado(Long carritoId) {
    try {
      Optional<Carrito> carritoOpt = carritoRepository.findById(carritoId);
      if (carritoOpt.isPresent()) {
        Carrito carrito = carritoOpt.get();
        return (
          carrito.getExpiraEn() != null &&
          LocalDateTime.now().isAfter(carrito.getExpiraEn())
        );
      }
      return true;
    } catch (Exception e) {
      log.warn(
        "Error verificando expiración del carrito {}: {}",
        carritoId,
        e.getMessage()
      );
      return false;
    }
  }

  @Override
  public void validarExistenciaUsuario(Long usuarioId) {
    try {
      // TODO: Implementar validación con UsuarioFeignClient
      if (usuarioId == null || usuarioId <= 0) {
        throw new BusinessException(
          "ID de usuario inválido",
          "USUARIO_ID_INVALIDO"
        );
      }

      log.debug("Usuario {} validado exitosamente", usuarioId);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error(
        "Error validando existencia del usuario {}: {}",
        usuarioId,
        e.getMessage(),
        e
      );
      throw new BusinessException(
        "Error validando usuario",
        "USUARIO_VALIDACION_ERROR",
        e
      );
    }
  }

  /**
   * Obtiene la IP del cliente desde el contexto de la request.
   * TODO: Implementar obtención real de IP desde HttpServletRequest
   */
  private String obtenerIpCliente() {
    return "127.0.0.1"; // Stub implementation
  }
}
