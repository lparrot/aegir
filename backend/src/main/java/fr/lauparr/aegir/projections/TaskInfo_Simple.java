package fr.lauparr.aegir.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.lauparr.aegir.utils.DateTimeUtils;

import java.time.LocalDateTime;

public interface TaskInfo_Simple {
  Long getId();

  String getName();

  String getDescription();

  @JsonFormat(pattern = DateTimeUtils.PATTERN_DATE_TIME)
  LocalDateTime getCreatedAt();

  @JsonFormat(pattern = DateTimeUtils.PATTERN_DATE_TIME)
  LocalDateTime getAssignedAt();

  UserInfo_Datatable getAssigned();
}
