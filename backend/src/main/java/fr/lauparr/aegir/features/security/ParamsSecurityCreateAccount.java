package fr.lauparr.aegir.features.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ParamsSecurityCreateAccount {
  private String username;
  private String password;
  private String firstname;
  private String lastname;
  private String job;
}
