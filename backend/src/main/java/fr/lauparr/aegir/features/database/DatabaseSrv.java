package fr.lauparr.aegir.features.database;

import fr.lauparr.aegir.entities.Workspace;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.repositories.WorkspaceRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseSrv {

  @Autowired
  private WorkspaceRepository workspaceRepository;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Autowired
  private DataSource dataSource;

  @Transactional(readOnly = true)
  public List<TableDto> getTables(Long workspaceId, boolean withColumns) throws SQLException {
    Workspace workspace = getWorkspaceById(workspaceId);

    String nameSlug = workspace.getSlug();
    ArrayList<TableDto> list = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      DatabaseMetaData metaData = connection.getMetaData();
      ResultSet resultSet = metaData.getTables(connection.getCatalog(), connection.getSchema(), "wk_" + nameSlug + "_%", null);

      while (resultSet.next()) {
        list.add(new TableDto()
          .setCatalog(resultSet.getString("TABLE_CAT"))
          .setName(resultSet.getString("TABLE_NAME"))
          .setRemarks(resultSet.getString("REMARKS"))
        );
      }
    }

    for (TableDto tableDto : list) {
      if (withColumns) {
        tableDto.setColumns(getColumns(tableDto.getName()));
      } else {
        tableDto.setColumns(null);
      }
    }

    return list;
  }

  @Transactional(readOnly = true)
  public List<TableColumnDto> getColumns(String tableName) throws SQLException {
    ArrayList<TableColumnDto> list = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
      ResultSet RSColumns = connection.getMetaData().getColumns(connection.getCatalog(), connection.getSchema(), tableName, null);


      while (RSColumns.next()) {
        TableColumnDto column = new TableColumnDto()
          .setCatalog(RSColumns.getString("TABLE_CAT"))
          .setTable(RSColumns.getString("TABLE_NAME"))
          .setName(RSColumns.getString("COLUMN_NAME"))
          .setTypeName(RSColumns.getString("TYPE_NAME"))
          .setSize(RSColumns.getString("COLUMN_SIZE"))
          .setPosition(RSColumns.getInt("ORDINAL_POSITION"))
          .setRemarks(RSColumns.getString("REMARKS"))
          .setDefaultValue(RSColumns.getString("COLUMN_DEF"))
          .setType(JDBCType.valueOf(RSColumns.getInt("DATA_TYPE")))
          .setNullable(RSColumns.getBoolean("IS_NULLABLE"))
          .setAutoincrement(RSColumns.getBoolean("IS_AUTOINCREMENT"));

        list.add(column);
      }

      ResultSet RSPrimaryKeys = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);
      while (RSPrimaryKeys.next()) {
        String pkName = RSPrimaryKeys.getString("COLUMN_NAME");
        list.stream().filter(column -> column.getName().equals(pkName)).forEach(column -> column.setPrimaryKey(true));
      }

      ResultSet RSForeignKeys = connection.getMetaData().getImportedKeys(connection.getCatalog(), connection.getSchema(), tableName);
      while (RSForeignKeys.next()) {
        String fkName = RSForeignKeys.getString("FKCOLUMN_NAME");
        String fkTableName = RSForeignKeys.getString("FKTABLE_NAME");
        String fkReference = RSForeignKeys.getString("PKCOLUMN_NAME");
        list.stream().filter(column -> column.getName().equals(fkName)).forEach(column -> {
          column.setForeignKey(true);
          column.setForeignKeyTableName(fkTableName);
          column.setReference(fkReference);
        });
      }

      ResultSet RSIndexInfo = connection.getMetaData().getIndexInfo(connection.getCatalog(), connection.getSchema(), tableName, true, false);
      while (RSIndexInfo.next()) {
        String name = RSIndexInfo.getString("COLUMN_NAME");
        boolean unique = !RSIndexInfo.getBoolean("NON_UNIQUE");

        list.stream().filter(column -> column.getName().equals(name)).forEach(column -> {
          column.setUniqueKey(unique);
        });

      }
    }

    return list;
  }

  public void editTable(String tableName, Long workspaceId, ParamsEditTable params) {
    if (tableName == null) {
      Workspace workspace = getWorkspaceById(workspaceId);

      KeyHolder holder = new GeneratedKeyHolder();

      SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("table_comment", params.getRemarks());

      jdbcTemplate.update("create table if not exists " + workspace.getWorkspaceTableName(params.getName()) + " (id int auto_increment, created_at datetime not null default now(), primary key (id)) comment :table_comment", parameters, holder);
    }
  }

  private Workspace getWorkspaceById(Long workspaceId) {
    return workspaceRepository.findById(workspaceId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.workspace")));
  }
}
