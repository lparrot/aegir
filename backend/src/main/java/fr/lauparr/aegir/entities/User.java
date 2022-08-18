package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@Cacheable
@Accessors(chain = true)
public class User implements UserDetails {

  @Id
  private Long id;

  @NotEmpty
  private String username;

  @JsonIgnore
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_user_profile"))
  private Profile profile;

  @JsonManagedReference("user_project")
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Project> projects = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @MapsId
  @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "FK_user_user_data"))
  private UserData userData;

  @PostPersist
  @PostUpdate
  public void postSave() {
    projects.forEach(project -> {
      project.setUser(this);
    });
  }

  public User addProject(Project project) {
    this.projects.add(project);
    return this;
  }

  public Claims getClaims() {
    final Claims claims = Jwts.claims();
    claims.put("id", this.id);
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
