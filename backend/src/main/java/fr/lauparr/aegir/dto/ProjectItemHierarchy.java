package fr.lauparr.aegir.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProjectItemHierarchy {
  private Long id;
  private String name;
}
