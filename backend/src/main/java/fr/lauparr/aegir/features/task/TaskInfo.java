package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.enums.EnumProjectItemType;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskInfo {
  Long getId();

  String getName();

  String getDescription();

  LocalDateTime getCreatedAt();

  LocalDateTime getAssignedAt();

  TaskProjectItemInfo getView();

  TaskUserInfo getAssigned();

  TaskUserInfo getUser();

  List<TaskCommentInfo> getComments();

  interface TaskUserInfo {
    Long getId();

    String getUsername();
  }

  interface TaskCommentInfo {
    Long getId();

    String getContent();

    LocalDateTime getCreatedAt();

    TaskUserInfo getUser();
  }

  interface TaskProjectItemInfo {
    Long getId();

    String getName();

    EnumProjectItemType getType();
  }
}
