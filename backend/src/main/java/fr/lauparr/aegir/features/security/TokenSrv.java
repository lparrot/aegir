package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.utils.DateTimeUtils;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TokenSrv {

  private static final SignatureAlgorithm signature = SignatureAlgorithm.HS256;
  @Getter
  @Value("${security.jwt.token-prefix:Bearer}")
  private String tokenPrefix;
  @Getter
  @Value("${security.jwt.header-name:Authorization}")
  private String headerName;
  @Getter
  @Value("${security.jwt.secret-key:S3cr3t_K3y*}")
  private String secretKey;

  @Autowired
  private AppUserDetailsSrv appUserDetailsSrv;

  /**
   * Création d'un token JWT avec les informations du compte utilisateur
   */
  public String createToken(final User user) {
    // Aucun token si l'utilisateur est vide
    if (user == null) {
      return null;
    }
    // Récupération des informations utilisateur
    final Claims claims = user.getClaims();

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

    // Récupération des id des rôles de l'utilisateur
    if (authorities != null) {
      claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    // Récupération du nom de compte de l'utilisateur
    claims.setSubject(user.getUsername());

    return Jwts.builder().setClaims(claims).signWith(TokenSrv.signature, this.secretKey).compact();
  }

  /**
   * Création d'un token JWT avec expiration
   */
  public String createToken(final Claims claims, final LocalDateTime expiration) {
    final JwtBuilder jwtBuilder = Jwts.builder().setClaims(claims);

    if (expiration != null) {
      jwtBuilder.setExpiration(DateTimeUtils.convertLocalDateTimeToDate(expiration));
    }

    return jwtBuilder.signWith(TokenSrv.signature, this.secretKey).compact();
  }

  /**
   * Récupération de l'utilisateur par rapport au token
   */
  public User getUser(final String token) {
    final Claims claims = this.getClaims(token);
    return (User) this.appUserDetailsSrv.loadUserByUsername(claims.getSubject());
  }

  /**
   * Récupération de l'utilisateur par rapport au token passé dans les headers de la requête
   */
  public User getUser(final HttpServletRequest request) {
    final String token = request.getHeader(this.headerName);
    return this.getUser(token);
  }

  /**
   * Récupération des informations contenues dans le token
   */
  public Claims getClaims(String token) {
    if (token != null) {
      token = token.replace(String.format("%s ", this.tokenPrefix), "");
      try {
        return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
      } catch (final JwtException e) {
        throw new JwtException("Erreur lors de la conversion du token: " + e.getMessage());
      }
    }
    return Jwts.claims();
  }

  /**
   * Récupération des informations contenues dans le token passé dans les headers de la requête
   */
  public Claims getClaims(final HttpServletRequest request) {
    return this.getClaims(request.getHeader(this.headerName));
  }
}
