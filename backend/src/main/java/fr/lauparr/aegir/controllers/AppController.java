package fr.lauparr.aegir.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/app")
public class AppController {

  @GetMapping("informations")
  public ResponseEntity<?> getInformations() {
    return ResponseEntity.ok(JsonNodeFactory.instance.objectNode().put("version", "0.0.1").put("title", "aegir"));
  }

}
