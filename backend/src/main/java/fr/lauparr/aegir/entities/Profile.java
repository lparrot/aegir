package fr.lauparr.aegir.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Cacheable
@Accessors(chain = true)
public class Profile implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String label;

  private boolean defaultProfile;

  @Column(name = "role")
  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "profile_role")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private List<Role> roles = new ArrayList<>();

  public Profile addRole(Role role) {
    this.roles.add(role);
    return this;
  }
}
