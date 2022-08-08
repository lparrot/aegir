package fr.lauparr.aegir.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RestApiResponse<T> extends BaseRestApi<T> {
  private T result;
  private Map<String, ?> params;
  private String type;

  public RestApiResponse() {
    this(true);
  }

  public RestApiResponse(boolean success) {
    this.setSuccess(success);
  }
}
