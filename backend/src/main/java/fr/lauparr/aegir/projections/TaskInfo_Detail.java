package fr.lauparr.aegir.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.lauparr.aegir.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskInfo_Detail {
  Long getId();

  String getName();

  String getDescription();

  @JsonFormat(pattern = DateTimeUtils.PATTERN_DATE_TIME)
  LocalDateTime getCreatedAt();

  @JsonFormat(pattern = DateTimeUtils.PATTERN_DATE_TIME)
  LocalDateTime getAssignedAt();

  Optional<TaskStatusInfo_Simple> getStatus();

  Optional<UserInfo_Simple> getAssigned();

  Optional<UserInfo_Simple> getUser();

  List<TaskCommentInfo_Simple> getComments();
}
