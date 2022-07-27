package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  private String username;

  @JsonIgnore
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_user_profile"))
  private Profile profile;

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Project> projects = new ArrayList<>();

  @Builder
  public User(String username, String password, Profile profile) {
    this.username = username;
    this.password = password;
    this.profile = profile;
  }

  public User addProject(Project project) {
    project.setUser(this);
    this.setProjects(new ArrayList<>(this.getProjects()));
    this.getProjects().add(project);
    return this;
  }

  public Claims getClaims() {
    final Claims claims = Jwts.claims();
    claims.put("id", this.getId());
    claims.put("username", this.getUsername());
    return claims;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.profile == null) {
      return AuthorityUtils.NO_AUTHORITIES;
    }
    return this.profile.getRoles();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
