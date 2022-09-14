package fr.lauparr.aegir.projections;

import java.util.List;

public interface BoardInfo_Detail {
  Long getId();

  String getName();

  String getDescription();

  List<TaskInfo_Detail> getTasks();
}
