package fr.lauparr.aegir.features.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamsAuthUpdateUserData {

  private String email;
  private String firstname;
  private String lastname;
  private String address;
  private String postalCode;
  private String city;
  private String about;

}
