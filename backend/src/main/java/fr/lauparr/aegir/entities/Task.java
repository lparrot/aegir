package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  @CreatedDate
  private LocalDateTime createdAt;

  private LocalDateTime assignedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("project_item_task")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_project_item_view"))
  private ProjectItem view;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_project"))
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_assigned"))
  private User assigned;

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_user"))
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "task", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<TaskComment> comments = new ArrayList<>();

  @PostPersist
  @PostUpdate
  public void postSave() {
    comments.forEach(taskComment -> {
      taskComment.setTask(this);
    });
  }

  public Task addComment(TaskComment comment) {
    comments.add(comment);
    return this;
  }
}
