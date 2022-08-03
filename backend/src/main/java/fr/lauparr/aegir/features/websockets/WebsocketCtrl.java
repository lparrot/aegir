package fr.lauparr.aegir.features.websockets;

import fr.lauparr.aegir.controllers.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/websockets")
public class WebsocketCtrl extends BaseController {

  @Autowired
  private WebsocketSrv websocketSrv;

  @GetMapping
  public ResponseEntity<?> get() {
    return ok(websocketSrv.getSessions());
  }

}
