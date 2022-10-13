package fr.lauparr.aegir.features.database;

import com.mysql.cj.MysqlType;
import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/databases")
public class DatabaseCtrl extends BaseController {

  @Autowired
  private DatabaseSrv databaseSrv;

  @GetMapping("/{workspaceId}/tables")
  public ResponseEntity<RestApiResponse<List<TableDto>>> getTables(@PathVariable("workspaceId") Long workspaceId, @RequestParam(value = "with_columns", defaultValue = "false") boolean withColumns) throws SQLException {
    return ok(databaseSrv.getTables(workspaceId, withColumns));
  }

  @GetMapping("{tableName}/columns")
  public ResponseEntity<RestApiResponse<List<TableColumnDto>>> getColumns(@PathVariable("tableName") String tableName) throws SQLException {
    return ok(databaseSrv.getColumns(tableName));
  }

  @GetMapping("/types")
  public ResponseEntity<RestApiResponse<List<MysqlType>>> getMysqlTypes() {
    return ok(databaseSrv.getMysqlTypes());
  }

  @PostMapping("/{workspaceId}/tables")
  public ResponseEntity<RestApiResponse<Void>> createTable(@PathVariable("workspaceId") Long workspaceId, @RequestBody @Valid ParamsEditTable params) {
    databaseSrv.editTable(null, workspaceId, params);
    return ok();
  }

  @PostMapping("/columns")
  public ResponseEntity<RestApiResponse<Void>> createColumn(@RequestBody @Valid ParamsEditColumn params) {
    databaseSrv.editColumn(params);
    return ok();
  }

  @DeleteMapping("{tableName}/columns/{columnName}")
  public ResponseEntity<RestApiResponse<Void>> removeColumn(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {
    databaseSrv.removeColumn(tableName, columnName);
    return ok();
  }

}
