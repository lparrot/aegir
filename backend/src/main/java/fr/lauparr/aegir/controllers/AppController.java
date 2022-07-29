package fr.lauparr.aegir.controllers;

import fr.lauparr.aegir.controllers.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/app")
public class AppController extends BaseController {

  @Autowired
  private InfoEndpoint infoEndpoint;

  @GetMapping("informations")
  public ResponseEntity<?> getInformations() {
    return this.ok(this.infoEndpoint.info());
  }

}
