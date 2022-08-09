package fr.lauparr.aegir.features.websockets;

import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.dto.WebsocketTypedMessage;
import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class WebsocketSrv {

  @Getter
  @Setter
  private List<SocketUserSession> sessions = new ArrayList<>();

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  public void sendMessage(String topic, EnumWebsocketMessageType type) {
    this.messagingTemplate.convertAndSend(topic, new WebsocketTypedMessage(type));
  }

  public void sendMessage(String topic, EnumWebsocketMessageType type, Object message) {
    this.messagingTemplate.convertAndSend(topic, new WebsocketTypedMessage(type).setData(message));
  }

  public void sendMessage(String topic, EnumWebsocketMessageType type, final Function<Map<String, Object>, Map<String, Object>> data) {
    this.messagingTemplate.convertAndSend(topic, new WebsocketTypedMessage(type, data));
  }

  public void sendMessageToUser(String username, String topic, EnumWebsocketMessageType type) {
    this.messagingTemplate.convertAndSendToUser(username, topic, new WebsocketTypedMessage(type));
  }

  public void sendMessageToUser(String username, String topic, EnumWebsocketMessageType type, Object message) {
    this.messagingTemplate.convertAndSendToUser(username, topic, new WebsocketTypedMessage(type).setData(message));
  }

  public void sendMessageToUser(String username, String topic, EnumWebsocketMessageType type, final Function<Map<String, Object>, Map<String, Object>> data) {
    this.messagingTemplate.convertAndSendToUser(username, topic, new WebsocketTypedMessage(type, data));
  }

  public SocketUserSession getSession(final Predicate<SocketUserSession> condition) {
    return this.sessions.stream().filter(condition).findFirst().orElse(null);
  }

  public void removeSession(Predicate<SocketUserSession> condition) {
    this.sessions = this.sessions.stream().filter(condition).collect(Collectors.toList());
  }

}
