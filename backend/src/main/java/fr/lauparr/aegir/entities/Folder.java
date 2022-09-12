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
public class Folder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_folder")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_folder_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_folder_workspace"))
  private Workspace workspace;

  @OneToMany(mappedBy = "folder", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Board> boards = new ArrayList<>();

  public Folder addBoard(Board board) {
    board.setFolder(this);
    board.setWorkspace(this.getWorkspace());
    this.boards.add(board);
    return this;
  }

}
