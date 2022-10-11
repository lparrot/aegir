package fr.lauparr.aegir.features.database;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/databases/{workspaceId}")
public class DatabaseCtrl extends BaseController {

  @Autowired
  private DatabaseSrv databaseSrv;

  @GetMapping("/tables")
  public ResponseEntity<RestApiResponse<List<TableDto>>> getTables(@PathVariable("workspaceId") Long workspaceId, @RequestParam(value = "with_columns", defaultValue = "false") boolean withColumns) throws SQLException {
    return ok(databaseSrv.getTables(workspaceId, withColumns));
  }

}
