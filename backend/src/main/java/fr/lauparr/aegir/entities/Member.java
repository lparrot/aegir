package fr.lauparr.aegir.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@Accessors(chain = true)
public class Member {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_project_member_user"))
  private User user;

  @Enumerated(EnumType.STRING)
  private Role role;

}
