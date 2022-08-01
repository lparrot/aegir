package fr.lauparr.aegir.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  @OneToOne(mappedBy = "userData", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  private User user;

}
