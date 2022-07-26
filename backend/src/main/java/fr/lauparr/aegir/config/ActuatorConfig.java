package fr.lauparr.aegir.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.AuditEventsEndpoint;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.trace.http.HttpTraceEndpoint;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.mappings.MappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.stream.Collectors;

@Configuration
public class ActuatorConfig {
  @Bean
  @ConditionalOnMissingBean
  public AuditEventRepository auditEventRepository() {
    return new InMemoryAuditEventRepository();
  }

  @Bean
  @ConditionalOnMissingBean
  public InfoEndpoint infoEndpoint(ObjectProvider<InfoContributor> infoContributors) {
    return new InfoEndpoint(infoContributors.orderedStream().collect(Collectors.toList()));
  }

  @Bean
  @ConditionalOnMissingBean
  public HttpTraceRepository httpTraceRepository() {
    return new InMemoryHttpTraceRepository();
  }

  @Bean
  @ConditionalOnMissingBean
  public MetricsEndpoint metricsEndpoint(MeterRegistry registry) {
    return new MetricsEndpoint(registry);
  }

  @Bean
  @ConditionalOnMissingBean
  public AuditEventsEndpoint auditEventsEndpoint(AuditEventRepository auditEventRepository) {
    return new AuditEventsEndpoint(auditEventRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  public HttpTraceEndpoint httpTraceEndpoint() {
    return new HttpTraceEndpoint(this.httpTraceRepository());
  }

  @Bean
  @ConditionalOnMissingBean
  public EnvironmentEndpoint environmentEndpoint(Environment environment) {
    return new EnvironmentEndpoint(environment);
  }

  @Bean
  @ConditionalOnMissingBean
  public MappingsEndpoint mappingsEndpoint(ApplicationContext applicationContext, ObjectProvider<MappingDescriptionProvider> descriptionProviders) {
    return new MappingsEndpoint(descriptionProviders.orderedStream().collect(Collectors.toList()), applicationContext);
  }
}
