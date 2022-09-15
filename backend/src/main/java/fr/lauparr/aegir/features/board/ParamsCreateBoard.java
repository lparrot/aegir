package fr.lauparr.aegir.features.board;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ParamsCreateBoard {

  @NotBlank
  private String name;

  private String description;

}
