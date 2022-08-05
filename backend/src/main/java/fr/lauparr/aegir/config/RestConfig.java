package fr.lauparr.aegir.config;

import fr.lauparr.aegir.controllers.base.BaseApiController;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
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

import java.util.ArrayList;
import java.util.Arrays;
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

        BaseApiController.class.getDeclaredMethod("get", String.class));
    }
  }

  @Bean
  public OpenApiCustomiser generatedApis() {

    return openApi -> {
      List<String> list = Arrays.asList("users", "profiles");

      for (String item : list) {
        String path = String.format("/api/%s", item);

        Operation operation = new Operation();
        operation.setParameters(new ArrayList<>());
        ApiResponses responses = new ApiResponses();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("OK");
        responses.addApiResponse("200", apiResponse);
        operation.setResponses(responses);
        Parameter parameter = new Parameter();
        parameter.setName("path");
        parameter.setDescription("Path");
        operation.setParameters(Arrays.asList(parameter));

        PathItem pathItem = new PathItem();
        pathItem.setGet(operation);
        openApi.getPaths().put(path, pathItem);
      }
    };
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.registerApiPaths();
  }
}
