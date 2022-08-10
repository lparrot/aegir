package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Accessors(chain = true)
public class TaskStatus implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @JsonBackReference("project_item_statuses")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_status_workspace"))
  private ProjectItem workspace;

}
