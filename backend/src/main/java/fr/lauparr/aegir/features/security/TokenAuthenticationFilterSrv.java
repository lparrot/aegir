package fr.lauparr.aegir.features.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lauparr.aegir.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class TokenAuthenticationFilterSrv extends OncePerRequestFilter {

  @Value("${app.security.jwt.header-name:Authorization}")
  private String headerName;
  @Value("${app.security.jwt.token-prefix:Bearer}")
  private String tokenPrefix;

  @Autowired
  private TokenSrv tokenSrv;

  public TokenAuthenticationFilterSrv() {
  }

  @Override
  @Transactional
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
    final String header = request.getHeader(this.headerName);
    boolean doFilter = true;
    if (header != null && header.startsWith(this.tokenPrefix)) {
      try {
        final User user = this.tokenSrv.getUser(request);
        if (user != null) {
          final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (final Exception e) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(e));
        doFilter = false;
      }
    }
    if (doFilter) {
      chain.doFilter(request, response);
    }
  }
}
