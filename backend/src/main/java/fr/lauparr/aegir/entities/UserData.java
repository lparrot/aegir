package fr.lauparr.aegir.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class UserData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String firstname;

  private String lastname;

  private String address;

  private String city;

  private String postalCode;

  private String about;

  @OneToOne(mappedBy = "userData", cascade = {CascadeType.ALL})
  @PrimaryKeyJoinColumn
  private User user;

  public String getFullname() {
    if (lastname == null && firstname == null) {
      return null;
    }

    if (lastname == null) {
      return firstname;
    }


    if (firstname == null) {
      return lastname;
    }

    return lastname + " " + firstname;
  }

}
