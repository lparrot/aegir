package fr.lauparr.aegir.features.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.lauparr.aegir.entities.UserData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDetailDto implements Serializable {

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
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String userUsername;

    @JsonIgnore
    private UserData userData;

    public String getFullname() {
      if (userData.getLastname() == null && userData.getFirstname() == null) {
        return null;
      }

      if (userData.getLastname() == null) {
        return userData.getFirstname();
      }


      if (userData.getFirstname() == null) {
        return userData.getLastname();
      }

      return userData.getLastname() + " " + userData.getFirstname();
    }
  }
}
