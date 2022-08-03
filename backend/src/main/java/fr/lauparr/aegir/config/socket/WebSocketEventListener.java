package fr.lauparr.aegir.config.socket;

import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import fr.lauparr.aegir.features.websockets.WebsocketSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Slf4j
@Service
@ApplicationScope
public class WebSocketEventListener {

  @Autowired
  private WebsocketSrv websocketSrv;

  @EventListener
  public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
    final SocketUserSession user = (SocketUserSession) event.getUser();
    if (user != null) {
      final SocketUserSession session = this.websocketSrv.getSession(socketUserSession -> socketUserSession.getUsername().equals(user.getUsername()));

      if (session == null) {
        this.websocketSrv.getSessions().add(user);
        this.websocketSrv.sendMessage("/topic/session", EnumWebsocketMessageType.CONNECT, data -> {
          if (event.getUser() != null) {
            data.put("user", event.getUser().getName());
          }
          return data;
        });
      }
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
    final SocketUserSession user = (SocketUserSession) event.getUser();
    if (user != null) {
      this.websocketSrv.removeSession(userSession -> !userSession.getUsername().equals(user.getUsername()));
    } else {
      final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
      final String sessionId = accessor.getSessionId();
      this.websocketSrv.removeSession(userSession -> !userSession.getSessionId().equals(sessionId));
    }
    this.websocketSrv.sendMessage("/topic/session", EnumWebsocketMessageType.DISCONNECT, data -> {
      if (event.getUser() != null) {
        data.put("user", event.getUser().getName());
      }
      return data;
    });

  }
}
