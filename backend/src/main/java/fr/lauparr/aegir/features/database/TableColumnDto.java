package fr.lauparr.aegir.features.database;

import com.mysql.cj.MysqlType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class TableColumnDto {
  private String catalog;
  private String table;
  private String name;
  private String typeName;
  private int size;
  private boolean nullable;
  private String remarks;
  private MysqlType type;
  private boolean autoincrement;
  private int position;
  private String defaultValue;

  private boolean primaryKey;
  private boolean uniqueKey;
  private boolean foreignKey;
  private String foreignKeyTableName;
  private String reference;
}
