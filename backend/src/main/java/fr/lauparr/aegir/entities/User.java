package fr.lauparr.aegir.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@Entity
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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @MapsId
  @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "FK_user_user_data"))
  private UserData userData;

  @JsonManagedReference("user_workspace")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<Workspace> workspaces = new HashSet<>();

  public User addWorkspace(Workspace workspace) {
    workspace.setUser(this);
    this.workspaces.add(workspace);
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

  public User setUsername(String username) {
    this.username = StringUtils.lowerCase(username, Locale.FRENCH);
    return this;
  }
}
