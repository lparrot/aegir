package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @JsonManagedReference("project_project_items")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> items = new ArrayList<>();

  @ElementCollection
  @CollectionTable(name = "project_member", joinColumns = @JoinColumn(name = "project_id"), foreignKey = @ForeignKey(name = "FK_project_member_project"))
  private List<ProjectMember> members = new ArrayList<>();

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_project")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_user"))
  private User user;

  @PostPersist
  @PostUpdate
  public void postSave() {
    items.forEach(projectItem -> {
      projectItem.setProject(this);
    });
  }

  public Project addProjectItem(ProjectItem projectItem) {
    this.items.add(projectItem);
    return this;
  }

  public Project addMember(ProjectMember projectMember) {
    this.members.add(projectMember);
    return this;
  }
}
