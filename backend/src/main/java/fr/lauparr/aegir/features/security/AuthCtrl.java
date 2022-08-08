package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.utils.DaoUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthCtrl extends BaseController {

  @Autowired
  private AuthSrv authSrv;
  @Autowired
  private TokenSrv tokenSrv;

  @PostMapping("/login")
  public ResponseEntity<RestApiResponse<JwtTokenDto>> login(@RequestBody final ParamsSecurityLogin params) {
    return this.ok(this.authSrv.login(params.getUsername(), params.getPassword()));
  }

  @GetMapping("/user")
  public ResponseEntity<RestApiResponse<Claims>> getUser(final HttpServletRequest request) {
    return this.ok(this.tokenSrv.getClaims(request));
  }

  @GetMapping("/user_data")
  public ResponseEntity<RestApiResponse<UserInfo>> getUserData(final Principal principal) {
    return this.ok(DaoUtils.convertToDto(this.authSrv.getUserData(principal.getName()), UserInfo.class));
  }

  @PostMapping
  public ResponseEntity<RestApiResponse<String>> createAccount(@RequestBody final ParamsSecurityCreateAccount params) {
    // TODO ajouter le user data pour la création de compte
    final User user = this.authSrv.createAccount(null, params);
    return this.ok(this.tokenSrv.createToken(user));
  }

}
