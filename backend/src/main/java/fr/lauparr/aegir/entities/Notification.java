package fr.lauparr.aegir.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_notification_project"))
  private Project project;

  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_notification_user"))
  private User user;

  @Builder
  public Notification(String name) {
    this.name = name;
  }
}
