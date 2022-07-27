package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  private List<ProjectItem> items = new ArrayList<>();

  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_user"))
  private User user;

  @Builder
  public Project(String name, @Singular List<ProjectItem> items) {
    this.name = name;
    this.items = items;
  }

  public Project addProjectItem(ProjectItem projectItem) {
    System.out.println("Project.addProjectItem");
    projectItem.setProject(this);
    this.setItems(new ArrayList<>(this.getItems()));
    this.getItems().add(projectItem);
    return this;
  }
}
