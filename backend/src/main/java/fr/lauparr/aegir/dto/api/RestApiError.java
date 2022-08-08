package fr.lauparr.aegir.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RestApiError extends BaseRestApi {

  private String message;
  private String type; // validation, exception, message ...
  private Map<String, Object> detail; // validation errors, exception details, message level ...

  public RestApiError() {
    setSuccess(false);
  }
}
