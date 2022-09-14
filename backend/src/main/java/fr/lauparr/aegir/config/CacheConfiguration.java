package fr.lauparr.aegir.config;

import fr.lauparr.aegir.constantes.CacheConstantes;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

  private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

  public CacheConfiguration() {
    jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
      CacheConfigurationBuilder
        .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(100))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3600)))
        .build()
    );
  }

  @Bean
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
    return hibernateProperties -> {
      hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
      hibernateProperties.put(ConfigSettings.MISSING_CACHE_STRATEGY, cacheManager);
    };
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      createCache(cm, CacheConstantes.CACHE_USER_TOKEN);
      // jhipster-needle-ehcache-add-entry
    };
  }

  private void createCache(javax.cache.CacheManager cm, String cacheName) {
    javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
    if (cache != null) {
      cache.clear();
    } else {
      cm.createCache(cacheName, jcacheConfiguration);
    }
  }
}
