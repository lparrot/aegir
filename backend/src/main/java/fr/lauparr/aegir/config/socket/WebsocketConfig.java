package fr.lauparr.aegir.config.socket;

import fr.lauparr.aegir.config.AutowireHelper;
import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.features.security.TokenSrv;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${app.security.jwt.header-name}")
  private String headerName;

  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
      .setAllowedOriginPatterns("*")
      .addInterceptors(this.httpSessionHandshakeInterceptor())
      .withSockJS();
  }

  @Override
  public void configureMessageBroker(final MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
  }

  @Override
  public void configureClientInboundChannel(final ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {

      @Override
      public Message<?> preSend(final Message<?> message, final MessageChannel channel) {

        final StompHeaderAccessor accessor =
          MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
          final String jwtToken = accessor.getFirstNativeHeader(WebsocketConfig.this.headerName);
          if (StringUtils.isNotBlank(jwtToken)) {
            final User user = AutowireHelper.getBean(TokenSrv.class).getUser(jwtToken);
            if (user != null) {
              final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(auth);
              final SocketUserSession userSession = new SocketUserSession(accessor.getSessionId(), user);
              accessor.setUser(userSession);
            }
          }
        }

        return message;
      }
    });
  }

  @Bean
  public HandshakeInterceptor httpSessionHandshakeInterceptor() {
    return new HandshakeInterceptor() {
      @Override
      public boolean beforeHandshake(final ServerHttpRequest request, final ServerHttpResponse response, final WebSocketHandler wsHandler, final Map<String, Object> attributes
      ) {
        if (request instanceof ServletServerHttpRequest) {
          final ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
          attributes.put("ipAddress", servletRequest.getRemoteAddress());
        }
        return true;
      }

      @Override
      public void afterHandshake(
        final ServerHttpRequest request,
        final ServerHttpResponse response,
        final WebSocketHandler wsHandler,
        final Exception exception
      ) {
      }
    };
  }
}
