package fr.lauparr.aegir.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.lauparr.aegir.entities.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.security.Principal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class SocketUserSession implements Principal {

  private Long id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String profil;
  private String sessionId;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime connectionDate;

  public SocketUserSession(final String sessionId, final User utilisateur) {
    this.sessionId = sessionId;
    this.id = utilisateur.getId();
    this.username = utilisateur.getUsername();
    this.firstname = utilisateur.getUserData().getFirstname();
    this.lastname = utilisateur.getUserData().getLastname();
    this.email = utilisateur.getUserData().getEmail();
    this.profil = utilisateur.getProfile().getLabel();
    this.connectionDate = LocalDateTime.now();
  }

  @Override
  public String getName() {
    return this.username;
  }
}
