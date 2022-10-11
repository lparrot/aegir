package fr.lauparr.aegir.controllers.base;

import fr.lauparr.aegir.features.generated_api.GeneratedApiSrv;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BaseApiController {

  private String item;
  private String path;
  private HttpServletRequest request;
  private GeneratedApiSrv service;

  public ResponseEntity<List<?>> get() {
    service.get();
    return ResponseEntity.ok(

    ).build();
  }

  public Object getOne(String path) {
    return path;
  }

}
