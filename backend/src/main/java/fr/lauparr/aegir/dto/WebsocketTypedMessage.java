package fr.lauparr.aegir.dto;

import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@Accessors(chain = true)
public class WebsocketTypedMessage {

  private EnumWebsocketMessageType type;
  private Object data;

  public WebsocketTypedMessage(final EnumWebsocketMessageType type) {
    this(type, data -> data);
  }

  public WebsocketTypedMessage(final EnumWebsocketMessageType type, final Function<Map<String, Object>, Map<String, Object>> data) {
    this.type = type;
    this.data = data.apply(new HashMap<>());
  }

}
