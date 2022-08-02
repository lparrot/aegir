package fr.lauparr.aegir.config.socket;

import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.dto.WebsocketTypedMessage;
import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@ApplicationScope
public class WebSocketEventListener {

  private final SimpMessageSendingOperations messagingTemplate;

  @Getter
  private List<SocketUserSession> sessions = new ArrayList<>();

  public WebSocketEventListener(final SimpMessageSendingOperations messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @EventListener
  public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
    final SocketUserSession user = (SocketUserSession) event.getUser();
    if (user != null) {
      final SocketUserSession session = this.sessions.stream().filter(userSession -> userSession.getUsername().equals(user.getUsername())).findFirst().orElse(null);

      if (session == null) {
        this.messagingTemplate.convertAndSend("/topic/session", new WebsocketTypedMessage(EnumWebsocketMessageType.CONNECT, data -> {
          if (event.getUser() != null) {
            data.put("user", event.getUser().getName());
          }
          return data;
        }));
      }
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
    final SocketUserSession user = (SocketUserSession) event.getUser();
    if (user != null) {
      this.sessions = this.sessions.stream().filter(userSession -> !userSession.getUsername().equals(user.getUsername())).collect(Collectors.toList());
    } else {
      final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
      final String sessionId = accessor.getSessionId();
      this.sessions = this.sessions.stream().filter(userSession -> !userSession.getSessionId().equals(sessionId)).collect(Collectors.toList());
    }
    this.messagingTemplate.convertAndSend("/topic/session", new WebsocketTypedMessage(EnumWebsocketMessageType.DISCONNECT, data -> {
      if (event.getUser() != null) {
        data.put("user", event.getUser().getName());
      }
      return data;
    }));

  }

  public SocketUserSession getSessionByUsername(final String username) {
    return this.sessions.stream().filter(dtoSocketUserSession -> dtoSocketUserSession.getUsername().equals(username)).findFirst().orElse(null);
  }
}
