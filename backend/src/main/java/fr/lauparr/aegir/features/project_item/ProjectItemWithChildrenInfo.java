package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.enums.EnumProjectItemType;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ProjectItemWithChildrenInfo {
  Long getId();

  String getName();

  EnumProjectItemType getType();

  @Value("#{target.parent?.id}")
  Long getParentId();

  List<ProjectItemWithChildrenInfo> getChildren();

  @Value("#{target.getItemNameHierarchy()}")
  String[] getItemHierarchy();
}
