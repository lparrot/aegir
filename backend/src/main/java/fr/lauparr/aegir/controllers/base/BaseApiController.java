package fr.lauparr.aegir.controllers.base;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.List;

@Component
public class BaseApiController {

  public ResponseEntity<List<?>> get(@PathParam("path") String path) {
    return ResponseEntity.ok(Collections.singletonList(path));
  }

  public Object getOne(String path) {
    return path;
  }

  public Object put(String path) {
    return path;
  }

}
