package fr.lauparr.aegir.dto.api;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ApiError extends BaseApi {

  private final String message;
  private final String type; // validation, exception, message ...
  private final Map<String, Object> detail; // validation errors, exception details, message level ...

  @Builder
  public ApiError(boolean success, String message, String type, Map<String, Object> detail) {
    super(success);
    this.message = message;
    this.type = type;
    this.detail = detail;
  }
}
