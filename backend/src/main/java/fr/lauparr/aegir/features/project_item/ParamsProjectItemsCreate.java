package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.enums.EnumProjectItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ParamsProjectItemsCreate {

  private Long parentId;
  private Long projectId;
  private String name;
  private EnumProjectItemType type;
  private List<ParamsProjectItemsCreateStatus> statuses = new ArrayList<>();

  @Getter
  @Setter
  public static class ParamsProjectItemsCreateStatus {
    private String name;
    private String color;
  }

}
