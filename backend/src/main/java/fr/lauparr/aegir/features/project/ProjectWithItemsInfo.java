package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.features.project_item.ProjectItemWithChildrenInfo;

import java.util.List;

public interface ProjectWithItemsInfo {
  Long getId();

  String getName();

  List<ProjectItemWithChildrenInfo> getItems();
}
