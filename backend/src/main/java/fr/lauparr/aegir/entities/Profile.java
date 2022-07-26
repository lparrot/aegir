package fr.lauparr.aegir.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
  private List<Role> roles = new ArrayList<>();

  @Builder
  public Profile(String label, boolean defaultProfile, @Singular List<Role> roles) {
    this.label = label;
    this.defaultProfile = defaultProfile;
    this.roles = roles;
  }
}
