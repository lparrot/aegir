package fr.lauparr.aegir.config;

import fr.lauparr.aegir.controllers.base.BaseApiController;
import fr.lauparr.aegir.dto.api.RestApiError;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.features.generated_api.GeneratedApiSrv;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.SneakyThrows;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class RestConfig implements ApplicationContextAware {

  Map<String, List<String>> list = new HashMap<>();

  @Autowired
  private RequestMappingHandlerMapping handlerMapping;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private GeneratedApiSrv generatedApiSrv;

  public RestConfig() {
    list.put("ccs", Arrays.asList("users", "profiles"));
    list.put("soleil", Collections.singletonList("users"));
  }

  @SneakyThrows
  public void registerApiPaths() {
    for (Map.Entry<String, List<String>> item : list.entrySet()) {
      for (String path : item.getValue()) {
        String apiPath = String.format("/api/%s/%s", item.getKey(), path);

        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());

        BaseApiController baseApiController = new BaseApiController()
          .setItem(item.getKey())
          .setPath(path)
          .setRequest(request)
          .setService(generatedApiSrv);

        this.handlerMapping.registerMapping(RequestMappingInfo.paths(apiPath).produces(MediaType.APPLICATION_JSON_VALUE).methods(RequestMethod.GET).options(options).build(),

          baseApiController,

          BaseApiController.class.getDeclaredMethod("get"));
      }
    }
  }

  @Bean
  public OpenApiCustomiser generatedApis(ServletContext servletContext) {
    Server server = new Server().description("Default server URL").url(servletContext.getContextPath());
    return openApi -> {
      openApi.info(new Info()
          .title("PPlanner")
          .description("OpenAPI Swagger pour projet PPlanner"))
        .servers(Collections.singletonList(server))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

      openApi.getComponents().getSchemas().putAll(ModelConverters.getInstance().read(RestApiResponse.class));
      openApi.getComponents().getSchemas().putAll(ModelConverters.getInstance().read(RestApiError.class));

      for (Map.Entry<String, List<String>> item : list.entrySet()) {
        for (String path : item.getValue()) {
          String apiPath = String.format("/api/%s/%s", item.getKey(), path);

          // Path findAll
          addPathItem(openApi, RequestMethod.GET, apiPath, "Get all");

          // Path findOne
          addPathItem(openApi, RequestMethod.GET, apiPath + "/{id}", "Get one");

          // Path update
          addPathItem(openApi, RequestMethod.PUT, apiPath + "/{id}", "Update");

          // Path create
          addPathItem(openApi, RequestMethod.POST, apiPath, "Create");

          // Path delete
          addPathItem(openApi, RequestMethod.DELETE, apiPath, "Delete");
        }
      }
    };
  }

  public void addPathItem(OpenAPI openApi, RequestMethod method, String path, String description) {
    PathItem pathItem = new PathItem();

    Schema<?> apiResponseSchema = new Schema<RestApiResponse>().$ref("#/components/schemas/" + RestApiResponse.class.getName());
    Schema<?> apiErrorSchema = new Schema<RestApiError>().$ref("#/components/schemas/" + RestApiError.class.getName());

    Operation operation = new Operation().operationId(method.name().toLowerCase()).description(description).tags(Collections.singletonList("generated")).responses(new ApiResponses().addApiResponse(HttpStatus.OK.getReasonPhrase(), new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType().schema(apiResponseSchema)))).addApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType().schema(apiErrorSchema)))).addApiResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType().schema(apiErrorSchema)))).addApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new ApiResponse().content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, new io.swagger.v3.oas.models.media.MediaType().schema(apiErrorSchema)))));

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
    this.registerApiPaths();
  }
}
