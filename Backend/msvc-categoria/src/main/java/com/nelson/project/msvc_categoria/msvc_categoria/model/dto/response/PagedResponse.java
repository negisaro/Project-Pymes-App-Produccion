package com.nelson.project.msvc_categoria.msvc_categoria.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * DTO especializado para respuestas paginadas.
 * Proporciona metadatos completos de paginación y navegación.
 *
 * @param <T> Tipo de contenido paginado
 *
 * @author Nelson Laza
 * @since 1.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estructura de respuesta para datos paginados")
public class PagedResponse<T> {

  /**
   * Lista de elementos de la página actual
   */
  @Schema(description = "Lista de elementos de la página actual")
  private List<T> content;

  /**
   * Metadatos de paginación
   */
  @Schema(description = "Información de paginación")
  private PageMetadata page;

  /**
   * Enlaces de navegación HATEOAS
   */
  @Schema(description = "Enlaces de navegación entre páginas")
  private NavigationLinks links;

  /**
   * Filtros aplicados en la consulta
   */
  @Schema(description = "Filtros aplicados en la consulta")
  private Map<String, Object> filters;

  /**
   * Ordenamiento aplicado
   */
  @Schema(description = "Criterios de ordenamiento aplicados")
  private List<SortCriteria> sorting;

  /**
   * Metadatos adicionales específicos del dominio
   */
  @Schema(description = "Metadatos adicionales específicos del contexto")
  private Map<String, Object> metadata;

  /**
   * Crea una respuesta paginada a partir de un Page de Spring
   */
  public static <T> PagedResponse<T> from(Page<T> page) {
    return PagedResponse.<T>builder()
      .content(page.getContent())
      .page(PageMetadata.from(page))
      .build();
  }

  /**
   * Crea una respuesta paginada con enlaces de navegación
   */
  public static <T> PagedResponse<T> from(Page<T> page, String baseUrl) {
    return PagedResponse.<T>builder()
      .content(page.getContent())
      .page(PageMetadata.from(page))
      .links(NavigationLinks.from(page, baseUrl))
      .build();
  }

  /**
   * Añade filtros aplicados
   */
  public PagedResponse<T> withFilters(Map<String, Object> filters) {
    this.filters = filters;
    return this;
  }

  /**
   * Añade criterios de ordenamiento
   */
  public PagedResponse<T> withSorting(List<SortCriteria> sorting) {
    this.sorting = sorting;
    return this;
  }

  /**
   * Añade metadatos adicionales
   */
  public PagedResponse<T> withMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Metadatos de paginación
   */
  @Data
  @Builder
  @Schema(description = "Metadatos de paginación")
  public static class PageMetadata {

    @Schema(description = "Número de página actual (base 0)", example = "0")
    private int number;

    @Schema(description = "Tamaño de página solicitado", example = "20")
    private int size;

    @Schema(
      description = "Total de elementos en todas las páginas",
      example = "150"
    )
    private long totalElements;

    @Schema(description = "Total de páginas disponibles", example = "8")
    private int totalPages;

    @Schema(
      description = "Número de elementos en la página actual",
      example = "20"
    )
    private int numberOfElements;

    @Schema(description = "Indica si es la primera página", example = "true")
    private boolean first;

    @Schema(description = "Indica si es la última página", example = "false")
    private boolean last;

    @Schema(description = "Indica si hay página anterior", example = "false")
    private boolean hasPrevious;

    @Schema(description = "Indica si hay página siguiente", example = "true")
    private boolean hasNext;

    @Schema(description = "Indica si la página está vacía", example = "false")
    private boolean empty;

    public static PageMetadata from(Page<?> page) {
      return PageMetadata.builder()
        .number(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .numberOfElements(page.getNumberOfElements())
        .first(page.isFirst())
        .last(page.isLast())
        .hasPrevious(page.hasPrevious())
        .hasNext(page.hasNext())
        .empty(page.isEmpty())
        .build();
    }
  }

  /**
   * Enlaces de navegación HATEOAS
   */
  @Data
  @Builder
  @Schema(description = "Enlaces de navegación HATEOAS")
  public static class NavigationLinks {

    @Schema(description = "URL de la página actual")
    private String self;

    @Schema(description = "URL de la primera página")
    private String first;

    @Schema(description = "URL de la última página")
    private String last;

    @Schema(description = "URL de la página anterior")
    private String prev;

    @Schema(description = "URL de la página siguiente")
    private String next;

    public static NavigationLinks from(Page<?> page, String baseUrl) {
      NavigationLinksBuilder builder = NavigationLinks.builder()
        .self(buildUrl(baseUrl, page.getNumber(), page.getSize()))
        .first(buildUrl(baseUrl, 0, page.getSize()))
        .last(buildUrl(baseUrl, page.getTotalPages() - 1, page.getSize()));

      if (page.hasPrevious()) {
        builder.prev(buildUrl(baseUrl, page.getNumber() - 1, page.getSize()));
      }

      if (page.hasNext()) {
        builder.next(buildUrl(baseUrl, page.getNumber() + 1, page.getSize()));
      }

      return builder.build();
    }

    private static String buildUrl(String baseUrl, int page, int size) {
      return String.format("%s?page=%d&size=%d", baseUrl, page, size);
    }
  }

  /**
   * Criterio de ordenamiento
   */
  @Data
  @Builder
  @Schema(description = "Criterio de ordenamiento aplicado")
  public static class SortCriteria {

    @Schema(description = "Campo por el cual se ordena", example = "nombre")
    private String property;

    @Schema(description = "Dirección del ordenamiento", example = "ASC")
    private String direction;

    @Schema(
      description = "Indica si ignora mayúsculas/minúsculas",
      example = "true"
    )
    private boolean ignoreCase;

    @Schema(
      description = "Indica si los valores nulos van al inicio",
      example = "false"
    )
    private boolean nullsFirst;
  }
}
