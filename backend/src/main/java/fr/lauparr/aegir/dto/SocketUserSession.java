package fr.lauparr.aegir.dto;

import fr.lauparr.aegir.entities.User;
import lombok.*;

import java.security.Principal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketUserSession implements Principal {

  private Long id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String profil;
  private String sessionId;

  public SocketUserSession(final String sessionId, final User utilisateur) {
    this.sessionId = sessionId;
    this.id = utilisateur.getId();
    this.username = utilisateur.getUsername();
    this.firstname = utilisateur.getUserData().getFirstname();
    this.lastname = utilisateur.getUserData().getLastname();
    this.email = utilisateur.getUserData().getEmail();
    this.profil = utilisateur.getProfile().getLabel();
  }

  @Override
  public String getName() {
    return this.username;
  }
}
