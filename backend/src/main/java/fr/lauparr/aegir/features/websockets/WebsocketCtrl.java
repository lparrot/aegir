package fr.lauparr.aegir.features.websockets;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/websockets")
public class WebsocketCtrl extends BaseController {

  @Autowired
  private WebsocketSrv websocketSrv;

  @GetMapping
  public ResponseEntity<RestApiResponse<List<SocketUserSession>>> getWebsockets() {
    return ok(websocketSrv.getSessions());
  }

  @PutMapping
  public ResponseEntity<RestApiResponse<List<SocketUserSession>>> putWebsockets() {
    return ok(websocketSrv.getSessions());
  }

}
