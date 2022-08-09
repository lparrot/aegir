package fr.lauparr.aegir.controllers;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/app")
public class AppCtrl extends BaseController {

  @Autowired
  private InfoEndpoint infoEndpoint;

  @Operation(description = "Retourne les paramètres de type info.' du fichier application.properties")
  @GetMapping("informations")
  public ResponseEntity<RestApiResponse<Map<String, Object>>> getAppInformations() {
    return this.ok(this.infoEndpoint.info());
  }

}
