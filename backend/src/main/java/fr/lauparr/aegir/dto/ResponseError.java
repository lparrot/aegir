package fr.lauparr.aegir.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ResponseError extends ResponseBase {

  private final String error;
  private final String type; // validation, exception, message ...
  private final Map<String, Object> detail; // validation errors, exception details, message level ...

  @Builder
  public ResponseError(boolean success, String error, String type, Map<String, Object> detail) {
    super(success);
    this.error = error;
    this.type = type;
    this.detail = detail;
  }
}
