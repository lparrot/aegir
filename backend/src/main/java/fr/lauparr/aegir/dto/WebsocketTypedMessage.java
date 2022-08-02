package fr.lauparr.aegir.dto;

import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
public class WebsocketTypedMessage {
  private final EnumWebsocketMessageType type;
  private final Object data;

  @Builder
  public WebsocketTypedMessage(final EnumWebsocketMessageType type, final Object data) {
    this.type = type;
    this.data = data;
  }

  public WebsocketTypedMessage(final EnumWebsocketMessageType type) {
    this(type, data -> data);
  }

  public WebsocketTypedMessage(final EnumWebsocketMessageType type, final Function<Map<String, Object>, Map<String, Object>> data) {
    this.type = type;
    this.data = data.apply(new HashMap<>());
  }
}
