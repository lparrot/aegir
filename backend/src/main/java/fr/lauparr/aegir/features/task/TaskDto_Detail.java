package fr.lauparr.aegir.features.task;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDto_Detail implements Serializable {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime assignedAt;
  private Long statusId;
  private Long viewId;
  private Long projectId;
  private Long assignedId;
  private Long userId;
  private List<TaskCommentDto> comments;

  @Getter
  @Setter
  public static class TaskCommentDto implements Serializable {
    public String userDataFullname;
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userUsername;
  }
}
