package fr.lauparr.aegir.features.security;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenDto {
  private String token;
  private Claims claims;

  @Builder
  public JwtTokenDto(final String token, final Claims claims) {
    this.token = token;
    this.claims = claims;
  }
}
