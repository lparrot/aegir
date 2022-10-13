package fr.lauparr.aegir.features.database;

import com.mysql.cj.MysqlType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class ParamsEditColumn {
  @NotBlank
  private String name;
  private Integer size;
  @NotNull
  private MysqlType type;
  private boolean nullable;

  private String remarks;

  @NotBlank
  private String tableName;

  private String oldName;
}
