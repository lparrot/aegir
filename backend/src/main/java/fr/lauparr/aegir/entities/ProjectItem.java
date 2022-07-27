package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.*;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
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
  @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> children = new ArrayList<>();

  @JsonBackReference("project_project_items")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_project"))
  private Project project;

  @JsonIgnore
  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_item_user"))
  private User user;

  @Builder
  public ProjectItem(String name, EnumProjectItemType type, ProjectItem parent, @Singular List<ProjectItem> children) {
    this.name = name;
    this.type = type;
    this.parent = parent;
    this.children = children;
  }

  public ProjectItem addChild(ProjectItem projectItem) {
    projectItem.setParent(this);
    projectItem.setProject(this.getProject());
    this.setChildren(new ArrayList<>(this.getChildren()));
    this.getChildren().add(projectItem);
    return this;
  }
}
