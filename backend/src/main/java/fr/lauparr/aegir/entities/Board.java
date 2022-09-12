package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Cacheable
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_board")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_board_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_board_workspace"))
  private Workspace workspace;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_board_folder"))
  private Folder folder;

  @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Task> tasks = new ArrayList<>();

  public Board addTask(Task task) {
    task.setBoard(this);
    tasks.add(task);
    return this;
  }
}
