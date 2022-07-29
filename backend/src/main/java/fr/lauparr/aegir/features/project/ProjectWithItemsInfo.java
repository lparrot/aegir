package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.features.project_item.ProjectItemInfo;

import java.util.List;

public interface ProjectWithItemsInfo {
  Long getId();

  String getName();

  List<ProjectItemInfo> getItems();
}
