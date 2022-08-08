package fr.lauparr.aegir.features.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class JwtTokenDto {
  private String token;
  private Claims claims;
}
