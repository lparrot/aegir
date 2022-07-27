package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.enums.EnumProjectItemType;

import java.util.List;

public interface ProjectItemInfo {
  Long getId();

  String getName();

  EnumProjectItemType getType();

  List<ProjectItemInfo> getChildren();
}
