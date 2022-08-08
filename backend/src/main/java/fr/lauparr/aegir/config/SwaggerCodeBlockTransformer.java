package fr.lauparr.aegir.config;

import org.apache.commons.io.FileUtils;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

  public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
    super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon, objectMapperProvider);
  }

  @Override
  public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformer) throws IOException {
    if (resource.toString().contains("swagger-ui.css")) {
      File file = ResourceUtils.getFile("classpath:swagger-ui.min.css");
      final byte[] transformedContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8).getBytes();
      return new TransformedResource(resource, transformedContent);
    }
    return super.transform(request, resource, transformer);
  }

}
