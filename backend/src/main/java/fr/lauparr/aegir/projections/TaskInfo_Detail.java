package fr.lauparr.aegir.projections;

import java.time.LocalDateTime;

public interface TaskInfo_Detail {
  Long getId();

  String getName();

  String getDescription();

  LocalDateTime getCreatedAt();

  LocalDateTime getAssignedAt();

  UserInfo_Datatable getAssigned();
}
