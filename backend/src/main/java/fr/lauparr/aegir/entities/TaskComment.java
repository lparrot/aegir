package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Cacheable
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class TaskComment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(columnDefinition = "text")
  private String content;

  @CreatedDate
  private LocalDateTime createdAt;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private Task task;

  @CreatedBy
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_task_comment_user"))
  private User user;

}
