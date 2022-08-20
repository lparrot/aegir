package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.lauparr.aegir.dto.ProjectItemHierarchy;
import fr.lauparr.aegir.enums.EnumProjectItemType;
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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Cacheable
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Long.class)
public class ProjectItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private EnumProjectItemType type;

  @JsonBackReference("project_item_children")
  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_parent"))
  private ProjectItem parent;

  @JsonBackReference("project_project_items")
  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_project"))
  private Project project;

  @JsonIgnore
  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_user"))
  private User user;

  @JsonManagedReference("project_item_children")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> children = new ArrayList<>();

  @JsonManagedReference("project_item_statuses")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<TaskStatus> statuses = new ArrayList<>();

  @JsonManagedReference("project_item_task")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "view", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<Task> tasks = new ArrayList<>();

  public void preRemove() {
    parent = null;
    project = null;
    user = null;
    removeChildren();
    removeTasks();
    removeStatuses();
  }

  @JsonIgnore
  public List<ProjectItemHierarchy> getItemHierarchy() {
    List<ProjectItemHierarchy> items = new ArrayList<>();
    items.add(new ProjectItemHierarchy().setId(id).setName(name));
    ProjectItem current = this;

    while (current.getParent() != null) {
      items.add(new ProjectItemHierarchy().setId(current.getParent().getId()).setName(current.getParent().getName()));
      current = current.getParent();
    }
    Collections.reverse(items);
    return items;
  }

  public ProjectItem addChild(ProjectItem projectItem) {
    projectItem.setParent(this);
    projectItem.setProject(this.project);
    this.children.add(projectItem);
    return this;
  }

  public ProjectItem addTask(Task task) {
    if (Objects.equals(this.type, EnumProjectItemType.VIEW)) {
      task.setView(this);
      tasks.add(task);
    }
    return this;
  }

  public ProjectItem addStatus(TaskStatus taskStatus) {
    if (Objects.equals(this.type, EnumProjectItemType.WORKSPACE)) {
      taskStatus.setWorkspace(this);
      statuses.add(taskStatus);
    }
    return this;
  }

  public List<ProjectItem> getAllChildren() {
    List<ProjectItem> allChildren = new ArrayList<>();

    for (ProjectItem child : children) {
      allChildren.add(child);
      allChildren.addAll(child.getAllChildren());
    }

    return allChildren;
  }

  public void removeChildren() {
    children.clear();
  }

  public void removeTasks() {
    tasks.clear();
  }

  public void removeStatuses() {
    statuses.clear();
  }
}
