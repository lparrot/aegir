package fr.lauparr.aegir.features.board;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.projections.BoardInfo_Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardCtrl extends BaseController {

  @Autowired
  private BoardSrv boardSrv;

  @GetMapping("/{boardId}")
  public ResponseEntity<RestApiResponse<BoardInfo_Detail>> getBoardById(@PathVariable("boardId") Long boardId) {
    return this.ok(this.boardSrv.getBoardById(boardId));
  }

  @PostMapping("/{workspaceId}")
  public ResponseEntity<RestApiResponse<Void>> createBoard(@PathVariable("workspaceId") Long workspaceId, @RequestBody ParamsCreateBoard params) {
    this.boardSrv.createBoard(workspaceId, params);
    return this.ok();
  }


  @PostMapping("/{workspaceId}/folder")
  public ResponseEntity<RestApiResponse<Void>> createFolder(@PathVariable("workspaceId") Long workspaceId, @RequestBody ParamsCreateFolder params) {
    this.boardSrv.createFolder(workspaceId, params);
    return this.ok();
  }

}
