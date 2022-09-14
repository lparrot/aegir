package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

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

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<Task> tasks = new HashSet<>();

  public Board addTask(Task task) {
    task.setBoard(this);
    tasks.add(task);
    return this;
  }
}
