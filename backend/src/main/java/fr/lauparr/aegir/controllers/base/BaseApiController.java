package fr.lauparr.aegir.controllers.base;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseApiController {

  public ResponseEntity<List<?>> get() {
    return ResponseEntity.ok().build();
  }

  public Object getOne(String path) {
    return path;
  }

}
