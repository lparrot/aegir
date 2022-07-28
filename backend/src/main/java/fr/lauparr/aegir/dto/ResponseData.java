package fr.lauparr.aegir.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ResponseData extends ResponseBase {

  private final Object data;
  private final Map<String, ?> params;
  private final String type;

  @Builder
  public ResponseData(boolean success, Object data, String type, Map<String, ?> params) {
    super(success);
    this.data = data;
    this.type = type;
    this.params = params;
  }

}
