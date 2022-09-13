package fr.lauparr.aegir.features.board;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.projections.BoardInfo_Detail;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardCtrl extends BaseController {

  @Autowired
  private BoardSrv boardSrv;

  @GetMapping("/{boardId}")
  public ResponseEntity<RestApiResponse<BoardInfo_Detail>> getBoardById(@PathVariable("boardId") Long boardId) {
    return this.ok(DaoUtils.convertToDto(this.boardSrv.getBoardById(boardId), BoardInfo_Detail.class));
  }

}
