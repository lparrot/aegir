package fr.lauparr.aegir.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import fr.lauparr.aegir.entities.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public Repositories repositories(ApplicationContext context) {
    return new Repositories(context);
  }

  @Bean
  public Module hibernate5Module() {
    Hibernate5Module hibernate5Module = new Hibernate5Module();
    hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
    hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
    return hibernate5Module;
  }

  @Bean
  public SpelAwareProxyProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }

  @Bean
  public AuditorAware<User> auditorProvider() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
      .map(SecurityContext::getAuthentication)
      .filter(Authentication::isAuthenticated)
      .map(Authentication::getPrincipal)
      .filter(o -> !SecurityConfig.ROLE_ANONYMOUS.equals(o))
      .map(User.class::cast);
  }

}
