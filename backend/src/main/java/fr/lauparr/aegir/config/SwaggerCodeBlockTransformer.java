package fr.lauparr.aegir.config;

import org.apache.commons.io.IOUtils;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

  @Autowired
  ResourceLoader resourceLoader;

  public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
    super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon, objectMapperProvider);
  }

  @Override
  public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformer) throws IOException {
    if (resource.toString().contains("swagger-ui.css")) {
      try {
        Resource customCssResource = resourceLoader.getResource("classpath:swagger-ui.min.css");
        final byte[] transformedContent = IOUtils.toString(customCssResource.getInputStream(), StandardCharsets.UTF_8).getBytes();
        return new TransformedResource(resource, transformedContent);
      } catch (FileNotFoundException e) {
        return super.transform(request, resource, transformer);
      }
    }
    return super.transform(request, resource, transformer);
  }

}
