package fr.lauparr.aegir.controllers.abstracts;

import fr.lauparr.aegir.dto.ResponseData;
import fr.lauparr.aegir.exceptions.TaggedApplicationException;
import fr.lauparr.aegir.utils.ControllerUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.logging.Level;

public abstract class AbstractController {

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
    return ResponseEntity.status(status).body(ResponseData.builder().success(true).build());
  }

  /**
   * Réponse OK avec données
   */
  public ResponseEntity<?> ok(Object data) {
    return ok(data, 200, null);
  }

  /**
   * Réponse OK avec données et paramètres
   */
  public ResponseEntity<?> ok(Object data, Map<String, ?> params) {
    return ok(data, 200, params);
  }

  /**
   * Réponse OK avec données, un statut et des paramètres
   */
  public ResponseEntity<?> ok(Object data, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ResponseData.builder().success(true).data(data).params(params).build());
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
  public ResponseEntity<?> okWithType(String type, Object data) {
    return okWithType(type, data, 200, null);
  }

  /**
   * Réponse OK avec données, un type et des paramètres
   */
  public ResponseEntity<?> okWithType(String type, Object data, Map<String, ?> params) {
    return okWithType(type, data, 200, params);
  }

  /**
   * Réponse OK avec données, type, un statut et des paramètres
   */
  public ResponseEntity<?> okWithType(String type, Object data, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ResponseData.builder().success(true).type(type).data(data).params(params).build());
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
    return ResponseEntity.status(status).body(ResponseData.builder().success(false).build());
  }

  /**
   * Réponse OK avec erreur fonctionnelle et données
   */
  public ResponseEntity<?> notOk(Object data) {
    return notOk(data, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données et paramètres
   */
  public ResponseEntity<?> notOk(Object data, Map<String, ?> params) {
    return notOk(data, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, statut et paramètres
   */
  public ResponseEntity<?> notOk(Object data, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ResponseData.builder().success(false).data(data).params(params).build());
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
  public ResponseEntity<?> notOkWithType(String type, Object data) {
    return notOkWithType(type, data, 200, null);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type et paramètres
   */
  public ResponseEntity<?> notOkWithType(String type, Object data, Map<String, ?> params) {
    return notOkWithType(type, data, 200, params);
  }

  /**
   * Réponse OK avec erreur fonctionnelle, données, type, statut et paramètres
   */
  public ResponseEntity<?> notOkWithType(String type, Object data, int status, Map<String, ?> params) {
    return ResponseEntity.status(status).body(ResponseData.builder().success(false).type(type).data(data).params(params).build());
  }

  /**
   * Réponse erreur avec message
   */
  public ResponseEntity<?> message(String message) {
    return message(message, Level.SEVERE);
  }

  /**
   * Réponse erreur avec message et niveau
   */
  public ResponseEntity<?> message(String message, Level level) {
    return message(message, level, 200);
  }

  /**
   * Réponse erreur avec message, niveau et un statut
   */
  public ResponseEntity<?> message(String message, Level level, int status) {
    return ResponseEntity.status(status).body(ControllerUtils.createMessageResponse(level, message));
  }

  /**
   * Réponse binaire
   */
  public ResponseEntity<?> file(byte[] data) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

  /**
   * Réponse fichier pdf
   */
  public ResponseEntity<?> pdf(byte[] data) {
    try {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
    } catch (Exception e) {
      throw new TaggedApplicationException("http", e);
    }
  }

}