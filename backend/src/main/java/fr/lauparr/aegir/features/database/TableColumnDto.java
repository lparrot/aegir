package fr.lauparr.aegir.features.database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.JDBCType;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TableColumnDto {
  private String catalog;
  private String table;
  private String name;
  private String typeName;
  private String size;
  private boolean nullable;
  private String remarks;
  private JDBCType type;
  private boolean autoincrement;
  private int position;
  private String defaultValue;

  private boolean primaryKey;
  private boolean uniqueKey;
  private boolean foreignKey;
  private String foreignKeyTableName;
  private String reference;
}
