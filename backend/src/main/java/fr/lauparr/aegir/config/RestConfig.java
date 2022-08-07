package fr.lauparr.aegir.config;

import fr.lauparr.aegir.controllers.base.BaseApiController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.SneakyThrows;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RestConfig implements ApplicationContextAware {

  @Autowired
  private RequestMappingHandlerMapping handlerMapping;
  @Autowired
  private BaseApiController baseApiController;
  private ApplicationContext applicationContext;

  @SneakyThrows
  public void registerApiPaths() {
    List<String> list = Arrays.asList("users", "profiles");

    for (String item : list) {
      String path = String.format("/api/%s", item);

      RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
      options.setPatternParser(new PathPatternParser());

      this.handlerMapping.registerMapping(
        RequestMappingInfo.paths(path)
          .produces(MediaType.APPLICATION_JSON_VALUE)
          .methods(RequestMethod.GET)
          .options(options).build(),

        baseApiController,

        BaseApiController.class.getDeclaredMethod("get"));
    }
  }

  @Bean
  public OpenApiCustomiser generatedApis() {

    return openApi -> {
      openApi.info(new Info().title("PPlanner").description("OpenAPI Swagger pour projet PPlanner"));

      List<String> list = Arrays.asList("users", "profiles");

      for (String item : list) {
        String path = String.format("/api/%s", item);

        // Path findAll
        addPathItem(openApi, RequestMethod.GET, path, "Get all");

        // Path findOne
        addPathItem(openApi, RequestMethod.GET, path + "/{id}", "Get one");

        // Path update
        addPathItem(openApi, RequestMethod.PUT, path + "/{id}", "Update");

        // Path create
        addPathItem(openApi, RequestMethod.POST, path, "Create");

        // Path delete
        addPathItem(openApi, RequestMethod.DELETE, path, "Delete");
      }
    };
  }

  public void addPathItem(OpenAPI openApi, RequestMethod method, String path, String description) {
    PathItem pathItem = new PathItem();

    Operation operation = new Operation()
      .operationId(method.name().toLowerCase())
      .description(description)
      .tags(Collections.singletonList("generated"))
      .responses(new ApiResponses()
        .addApiResponse("200", new ApiResponse().description("OK"))
        .addApiResponse("401", new ApiResponse().description("Not found"))
        .addApiResponse("404", new ApiResponse().description("Access denied"))
        .addApiResponse("500", new ApiResponse().description("Server error")));

    if (openApi.getPaths().get(path) != null) {
      pathItem = openApi.getPaths().get(path);
    }

    switch (method) {
      case GET:
        pathItem.get(operation);
        break;
      case PUT:
        pathItem.put(operation);
        break;
      case POST:
        pathItem.post(operation);
        break;
      case DELETE:
        pathItem.delete(operation);
        break;
      case PATCH:
        pathItem.patch(operation);
        break;
      default:
        throw new IllegalArgumentException("Méthode " + method.name() + " non gérée");
    }

    openApi.path(path, pathItem);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.registerApiPaths();
  }
}
