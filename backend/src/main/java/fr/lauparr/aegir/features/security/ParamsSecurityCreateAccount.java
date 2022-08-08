package fr.lauparr.aegir.features.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ParamsSecurityCreateAccount {
  private String username;
  private String password;
  private String firstname;
  private String lastname;
  private String job;
}
