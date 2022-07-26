package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.utils.DaoUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthCtrl {

  @Autowired
  private AuthSrv authSrv;
  @Autowired
  private TokenSrv tokenSrv;

  @PostMapping("/login")
  public JwtTokenDto login(@RequestBody final ParamsSecurityLogin params) {
    return this.authSrv.login(params.getUsername(), params.getPassword());
  }

  @GetMapping("/user")
  public Claims getUser(final HttpServletRequest request) {
    return this.tokenSrv.getClaims(request);
  }

  @GetMapping("/user_data")
  public UserInfo getUserData(final Principal principal) {
    return DaoUtils.convertToDto(this.authSrv.getUserData(principal.getName()), UserInfo.class);
  }

  @PostMapping
  public String createAccount(@RequestBody final ParamsSecurityCreateAccount params) {
    final User user = this.authSrv.createAccount(params);
    return this.tokenSrv.createToken(user);
  }

}
