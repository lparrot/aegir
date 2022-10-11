package fr.lauparr.aegir.features.database;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class TableDto {
  private String catalog;
  private String name;
  private String remarks;

  private List<TableColumnDto> columns = new ArrayList<>();
}
