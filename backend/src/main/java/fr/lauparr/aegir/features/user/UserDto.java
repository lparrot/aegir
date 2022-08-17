package fr.lauparr.aegir.features.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto implements Serializable {

  private Long id;
  @NotEmpty
  private String username;
  private Long profileId;
  private String profileLabel;
  private List<ProjectDto> projects = new ArrayList<>();
  private Long userDataId;
  private String userDataEmail;
  private String userDataFirstname;
  private String userDataLastname;
  private String userDataAddress;
  private String userDataCity;
  private String userDataPostalCode;
  private String userDataAbout;
  private String userDataFullname;

  @Data
  public static class ProjectDto implements Serializable {
    private Long id;
    private String name;
  }
}
