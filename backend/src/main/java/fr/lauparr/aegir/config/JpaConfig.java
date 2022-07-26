package fr.lauparr.aegir.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.support.Repositories;

@Configuration
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

}
