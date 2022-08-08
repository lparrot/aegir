package fr.lauparr.aegir.controllers.base;

import fr.lauparr.aegir.dto.api.RestApiError;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.exceptions.TaggedApplicationException;
import fr.lauparr.aegir.utils.ControllerUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class BaseController {

  /**
   * Réponse OK sans donnée
   */
  public <T> ResponseEntity<RestApiResponse<T>> ok() {
    return ok(200);
  }

  /**
   * Réponse OK sans donnée et un statut
   */
  private <T> ResponseEntity<RestApiResponse<T>> ok(int status) {
    return ResponseEntity.status(status).body(new RestApiResponse());
  }

  /**
   * Réponse OK avec données
   */
  public <T> ResponseEntity<RestApiResponse<T>> ok(T result) {
    return ok(result, 200, null);
  }

  /**
   * Réponse OK avec données et paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> ok(T result, Map<String, ?> params) {
    return ok(result, 200, params);
  }

  /**
   * Réponse OK avec données, un statut et des paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> ok(T result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(new RestApiResponse<T>().setResult(result).setParams(params));
  }

  /**
   * Réponse OK sans donnée et avec un type
   */
  public <T> ResponseEntity<RestApiResponse<T>> okWithType(String type) {
    return okWithType(type, null);
  }

  /**
   * Réponse OK avec données et avec un type
   */
  public <T> ResponseEntity<RestApiResponse<T>> okWithType(String type, T result) {
    return okWithType(type, result, 200, null);
  }

  /**
   * Réponse OK avec données, un type et des paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> okWithType(String type, T result, Map<String, ?> params) {
    return okWithType(type, result, 200, params);
  }

  /**
   * Réponse OK avec données, type, un statut et des paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> okWithType(String type, T result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(new RestApiResponse<T>().setType(type).setResult(result).setParams(params));
  }

  /**
   * Réponse OK avec erreur fonctionnelle sans donnée
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOk() {
    return notOk(200);
  }

  /**
   * Réponse OK avec erreur fonctionnelle sans donnée et un statut
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOk(int status) {
    return ResponseEntity.status(status).body(new RestApiResponse<T>(false));
  }

  /**
   * Réponse OK avec erreur fonctionnelle et données
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOk(T result) {
    return notOk(result, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données et paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOk(T result, Map<String, ?> params) {
    return notOk(result, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, statut et paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOk(T result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(new RestApiResponse<T>(false).setResult(result).setParams(params));
  }

  /**
   * Réponse OK avec erreur fonctionnelle et type
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOkWithType(String type) {
    return notOkWithType(type, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données et type
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOkWithType(String type, T result) {
    return notOkWithType(type, result, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type et paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOkWithType(String type, T result, Map<String, ?> params) {
    return notOkWithType(type, result, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type, statut et paramètres
   */
  public <T> ResponseEntity<RestApiResponse<T>> notOkWithType(String type, T result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(new RestApiResponse<T>(false).setType(type).setResult(result).setParams(params));
  }

  /**
   * Réponse erreur avec message
   */
  public ResponseEntity<RestApiError> message(String message) {
    return message(message, "negative");
  }

  /**
   * Réponse erreur avec message et niveau
   */
  public ResponseEntity<RestApiError> message(String message, String color) {
    return message(message, color, 200);
  }

  /**
   * Réponse erreur avec message, niveau et un statut
   */
  public ResponseEntity<RestApiError> message(String message, String color, int status) {
    return ResponseEntity.status(status).body(ControllerUtils.createMessageResponse(color, message));
  }

  /**
   * Réponse binaire
   */
  public ResponseEntity<byte[]> file(byte[] result) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(result);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

  /**
   * Réponse fichier pdf
   */
  public ResponseEntity<byte[]> pdf(byte[] result) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(result);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

}
