package fr.lauparr.aegir.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.lauparr.aegir.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TaskCommentInfo_Simple {
  Long getId();

  String getContent();

  @JsonFormat(pattern = DateTimeUtils.PATTERN_DATE_TIME)
  LocalDateTime getCreatedAt();

  Optional<UserInfo_Simple> getUser();
}
