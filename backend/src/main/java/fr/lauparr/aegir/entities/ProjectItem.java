package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.*;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Cacheable
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
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_parent"))
  private ProjectItem parent;

  @JsonManagedReference("project_item_children")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> children = new ArrayList<>();

  @JsonManagedReference("project_item_statuses")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<TaskStatus> statuses = new ArrayList<>();

  @JsonBackReference("project_project_items")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_project"))
  private Project project;

  @JsonIgnore
  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_user"))
  private User user;

  @JsonManagedReference("project_item_task")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "view", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<Task> tasks = new ArrayList<>();

  @JsonIgnore
  public String[] getItemNameHierarchy() {
    List<String> items = new ArrayList<>();
    items.add(this.name);
    ProjectItem current = this;

    while (current.getParent() != null) {
      items.add(current.getParent().getName());
      current = current.getParent();
    }

    return items.stream().sorted(Comparator.reverseOrder()).toArray(String[]::new);
  }

  @PostPersist
  @PostUpdate
  public void postSave() {
    this.children.forEach(projectItem -> {
      projectItem.setParent(this);
      projectItem.setProject(this.project);
    });
  }

  public ProjectItem addChild(ProjectItem projectItem) {
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
}
