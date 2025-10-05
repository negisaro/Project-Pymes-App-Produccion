package com.nelson.project.msvc_carrito.msvc_carrito.exception;

/**
 * Excepción para funcionalidades declaradas en la interfaz empresarial pero aún no implementadas.
 * Permite manejo uniforme en controladores (traducción a 501 Not Implemented, por ejemplo).
 */
public class FeatureNotImplementedException extends RuntimeException {

  private final String featureName;

  public FeatureNotImplementedException(String featureName) {
    super("Funcionalidad no implementada: " + featureName);
    this.featureName = featureName;
  }

  public FeatureNotImplementedException(String featureName, String detail) {
    super(
      "Funcionalidad no implementada: " +
      featureName +
      (detail != null ? " - " + detail : "")
    );
    this.featureName = featureName;
  }

  public String getFeatureName() {
    return featureName;
  }
}
