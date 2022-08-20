package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.dto.ProjectItemHierarchy;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ProjectItemInfo {
  Long getId();

  String getName();

  EnumProjectItemType getType();

  @Value("#{target.parent?.id}")
  Long getParentId();

  @Value("#{target.getItemHierarchy()}")
  List<ProjectItemHierarchy> getItemHierarchy();
}
