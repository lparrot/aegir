package fr.lauparr.aegir.controllers.base;

import fr.lauparr.aegir.dto.api.ApiResponse;
import fr.lauparr.aegir.exceptions.TaggedApplicationException;
import fr.lauparr.aegir.utils.ControllerUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class BaseController {

  /**
   * Réponse OK sans donnée
   */
  public ResponseEntity<?> ok() {
    return ok(200);
  }

  /**
   * Réponse OK sans donnée et un statut
   */
  private ResponseEntity<?> ok(int status) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(true).build());
  }

  /**
   * Réponse OK avec données
   */
  public ResponseEntity<?> ok(Object result) {
    return ok(result, 200, null);
  }

  /**
   * Réponse OK avec données et paramètres
   */
  public ResponseEntity<?> ok(Object result, Map<String, ?> params) {
    return ok(result, 200, params);
  }

  /**
   * Réponse OK avec données, un statut et des paramètres
   */
  public ResponseEntity<?> ok(Object result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(true).result(result).params(params).build());
  }

  /**
   * Réponse OK sans donnée et avec un type
   */
  public ResponseEntity<?> okWithType(String type) {
    return okWithType(type, null);
  }

  /**
   * Réponse OK avec données et avec un type
   */
  public ResponseEntity<?> okWithType(String type, Object result) {
    return okWithType(type, result, 200, null);
  }

  /**
   * Réponse OK avec données, un type et des paramètres
   */
  public ResponseEntity<?> okWithType(String type, Object result, Map<String, ?> params) {
    return okWithType(type, result, 200, params);
  }

  /**
   * Réponse OK avec données, type, un statut et des paramètres
   */
  public ResponseEntity<?> okWithType(String type, Object result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(true).type(type).result(result).params(params).build());
  }

  /**
   * Réponse OK avec erreur fonctionnelle sans donnée
   */
  public ResponseEntity<?> notOk() {
    return notOk(200);
  }

  /**
   * Réponse OK avec erreur fonctionnelle sans donnée et un statut
   */
  public ResponseEntity<?> notOk(int status) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(false).build());
  }

  /**
   * Réponse OK avec erreur fonctionnelle et données
   */
  public ResponseEntity<?> notOk(Object result) {
    return notOk(result, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données et paramètres
   */
  public ResponseEntity<?> notOk(Object result, Map<String, ?> params) {
    return notOk(result, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, statut et paramètres
   */
  public ResponseEntity<?> notOk(Object result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(false).result(result).params(params).build());
  }

  /**
   * Réponse OK avec erreur fonctionnelle et type
   */
  public ResponseEntity<?> notOkWithType(String type) {
    return notOkWithType(type, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données et type
   */
  public ResponseEntity<?> notOkWithType(String type, Object result) {
    return notOkWithType(type, result, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type et paramètres
   */
  public ResponseEntity<?> notOkWithType(String type, Object result, Map<String, ?> params) {
    return notOkWithType(type, result, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type, statut et paramètres
   */
  public ResponseEntity<?> notOkWithType(String type, Object result, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ApiResponse.builder().success(false).type(type).result(result).params(params).build());
  }

  /**
   * Réponse erreur avec message
   */
  public ResponseEntity<?> message(String message) {
    return message(message, "negative");
  }

  /**
   * Réponse erreur avec message et niveau
   */
  public ResponseEntity<?> message(String message, String color) {
    return message(message, color, 200);
  }

  /**
   * Réponse erreur avec message, niveau et un statut
   */
  public ResponseEntity<?> message(String message, String color, int status) {
    return ResponseEntity.status(status).body(ControllerUtils.createMessageResponse(color, message));
  }

  /**
   * Réponse binaire
   */
  public ResponseEntity<?> file(byte[] result) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(result);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

  /**
   * Réponse fichier pdf
   */
  public ResponseEntity<?> pdf(byte[] result) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(result);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

}
