package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.slugify.Slugify;
import fr.lauparr.aegir.entities.abstracts.SoftDeletableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Workspace {

  public static final Slugify SLUGIFY = new Slugify().withLowerCase(true).withUnderscoreSeparator(true);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(length = 10)
  private String abbreviation;

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

  @Where(clause = SoftDeletableEntity.SOFT_DELETED_CLAUSE)
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<Board> boards = new HashSet<>();

  @Where(clause = SoftDeletableEntity.SOFT_DELETED_CLAUSE)
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<Folder> folders = new HashSet<>();

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<TaskStatus> statuses = new HashSet<>();

  public List<Board> getBoardsNotInFolder() {
    return this.boards.stream().filter(board -> Objects.isNull(board.getFolder())).collect(Collectors.toList());
  }

  public String getSlug() {
    return SLUGIFY.slugify(this.getName());
  }

  public String getWorkspaceTableName(String tableName) {
    return String.format(
      "wk_%s_%s",
      StringUtils.substring(getSlug(), 0, 10),
      StringUtils.substring(SLUGIFY.slugify(tableName), 0, 40)
    );
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
