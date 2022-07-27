package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @JsonManagedReference("project_project_items")
  @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> items = new ArrayList<>();

  @CreatedBy
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference("user_project")
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_user"))
  private User user;

  @Builder
  public Project(String name, @Singular List<ProjectItem> items) {
    this.name = name;
    this.items = items;
  }

  public Project addProjectItem(ProjectItem projectItem) {
    projectItem.setProject(this);
    this.setItems(new ArrayList<>(this.getItems()));
    this.getItems().add(projectItem);
    return this;
  }
}
