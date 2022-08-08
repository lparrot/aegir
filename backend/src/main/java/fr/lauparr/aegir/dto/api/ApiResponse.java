package fr.lauparr.aegir.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class ApiResponse extends BaseApi {
  private Object result;
  private Map<String, ?> params;
  private String type;

  public ApiResponse() {
    this(true);
  }

  public ApiResponse(boolean success) {
    this.setSuccess(success);
  }
}
