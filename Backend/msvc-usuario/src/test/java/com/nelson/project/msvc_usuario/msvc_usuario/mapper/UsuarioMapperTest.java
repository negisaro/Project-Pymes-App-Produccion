package com.nelson.project.msvc_usuario.msvc_usuario.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.nelson.project.msvc_usuario.msvc_usuario.MsvcUsuarioApplication;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioCreateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.dto.UsuarioUpdateDto;
import com.nelson.project.msvc_usuario.msvc_usuario.model.entity.Usuario;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MsvcUsuarioApplication.class)
@ActiveProfiles("test")
public class UsuarioMapperTest {

  @Autowired
  private UsuarioMapper mapper;

  @Test
  void toEntity_fromCreateDto_ok() {
    UsuarioCreateDto dto = UsuarioCreateDto.builder()
      .name("John")
      .lastname("Doe")
      .username("jdoe")
      .password("secretPass1")
      .email("john@doe.com")
      .build();
    Usuario entity = mapper.toEntity(dto);
    assertThat(entity).isNotNull();
    assertThat(entity.getUsername()).isEqualTo("jdoe");
    assertThat(entity.getEmail()).isEqualTo("john@doe.com");
  }

  @Test
  void toDto_fromEntity_ok() {
    Usuario entity = Usuario.builder()
      .id(10L)
      .name("Jane")
      .lastname("Roe")
      .username("jroe")
      .email("jane@roe.com")
      .password("encoded")
      .active(true)
      .build();
    UsuarioDto dto = mapper.toDto(entity);
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(10L);
    assertThat(dto.getUsername()).isEqualTo("jroe");
  }

  @Test
  void updateEntityFromDto_partial_ok() {
    Usuario entity = Usuario.builder()
      .id(5L)
      .name("Old")
      .lastname("Name")
      .username("olduser")
      .email("old@mail.com")
      .password("x")
      .active(true)
      .build();
    UsuarioUpdateDto update = UsuarioUpdateDto.builder()
      .name("New")
      .email("new@mail.com")
      .build();
    mapper.updateEntityFromDto(update, entity);
    assertThat(entity.getName()).isEqualTo("New");
    assertThat(entity.getEmail()).isEqualTo("new@mail.com");
    assertThat(entity.getUsername()).isEqualTo("olduser");
  }

  @Test
  void toDtoList_handlesNullAndEmpty() {
    assertThat(mapper.toDtoList(List.of())).isEmpty();
  }

  @Test
  void toDto_null_returnsNull() {
    assertThat(mapper.toDto(null)).isNull();
  }

  @Test
  void toEntity_null_returnsNull() {
    assertThat(mapper.toEntity(null)).isNull();
  }
}
