package fr.lauparr.aegir.features.websockets;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.SocketUserSession;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.enums.EnumWebsocketMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @DeleteMapping
  public ResponseEntity<RestApiResponse<Void>> deleteWebsocketsByUsername(@RequestParam("username") String username) {
    websocketSrv.sendMessageToUser(username, "/topic/session", EnumWebsocketMessageType.CLOSE_SESSION);
    return ok();
  }

}
