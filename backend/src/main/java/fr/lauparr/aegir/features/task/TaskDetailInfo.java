package fr.lauparr.aegir.features.task;

import java.time.LocalDateTime;

public interface TaskDetailInfo {
  Long getId();

  String getName();

  String getDescription();

  LocalDateTime getCreatedAt();

  LocalDateTime getAssignedAt();

  TaskDetailUserInfo getAssigned();

  interface TaskDetailUserInfo {
    Long getId();

    String getUsername();
  }
}
