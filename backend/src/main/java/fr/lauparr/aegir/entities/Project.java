package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
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

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_project")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_user"))
  private User user;

  @JsonManagedReference("project_project_items")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> items = new ArrayList<>();

  @ElementCollection
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @CollectionTable(name = "project_member", joinColumns = @JoinColumn(name = "project_id"), foreignKey = @ForeignKey(name = "FK_project_member_project"))
  private List<ProjectMember> members = new ArrayList<>();

  public Project addProjectItem(ProjectItem projectItem) {
    projectItem.setProject(this);
    this.items.add(projectItem);
    return this;
  }

  public Project addMember(ProjectMember projectMember) {
    this.members.add(projectMember);
    return this;
  }
}
