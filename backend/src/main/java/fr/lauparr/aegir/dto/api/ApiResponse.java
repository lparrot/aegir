package fr.lauparr.aegir.dto.api;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ApiResponse extends BaseApi {

  private final Object result;
  private final Map<String, ?> params;
  private final String type;

  @Builder
  public ApiResponse(boolean success, Object result, String type, Map<String, ?> params) {
    super(success);
    this.result = result;
    this.type = type;
    this.params = params;
  }

}
