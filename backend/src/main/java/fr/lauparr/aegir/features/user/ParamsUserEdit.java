package fr.lauparr.aegir.features.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamsUserEdit {
  private String username;
  private String email;
  private String lastname;
  private String firstname;
  private String address;
  private String postalCode;
  private String city;
  private String about;
}
