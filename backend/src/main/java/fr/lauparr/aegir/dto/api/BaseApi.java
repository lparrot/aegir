package fr.lauparr.aegir.dto.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseApi {

  private boolean success;

}
