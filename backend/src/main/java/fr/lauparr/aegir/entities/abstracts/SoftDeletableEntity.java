package fr.lauparr.aegir.entities.abstracts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class SoftDeletableEntity {

  public static final String SOFT_DELETED_CLAUSE = "deleted_at is null";

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

}
