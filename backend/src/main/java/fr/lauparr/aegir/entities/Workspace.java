package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Cacheable
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Workspace {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_workspace")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_workspace_user"))
  private User user;

  @ElementCollection
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @CollectionTable(name = "workspace_member", joinColumns = @JoinColumn(name = "workspace_id"), foreignKey = @ForeignKey(name = "FK_member_workspace"))
  private List<Member> members = new ArrayList<>();

  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Folder> folders = new ArrayList<>();

  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<TaskStatus> statuses = new ArrayList<>();

  public List<Board> getBoardsNotInFolder() {
    return this.boards.stream().filter(board -> Objects.isNull(board.getFolder())).collect(Collectors.toList());
  }

  public Workspace addMember(Member member) {
    this.members.add(member);
    return this;
  }

  public Workspace addBoard(Board board) {
    board.setWorkspace(this);
    this.boards.add(board);
    return this;
  }

  public Workspace addFolder(Folder folder) {
    folder.setWorkspace(this);
    this.folders.add(folder);
    return this;
  }

  public Workspace addStatus(TaskStatus status) {
    status.setWorkspace(this);
    this.statuses.add(status);
    return this;
  }

}
