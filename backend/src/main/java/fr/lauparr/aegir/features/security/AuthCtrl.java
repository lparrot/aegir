package fr.lauparr.aegir.features.security;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.utils.DaoUtils;
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
  public ResponseEntity<?> login(@RequestBody final ParamsSecurityLogin params) {
    return this.ok(this.authSrv.login(params.getUsername(), params.getPassword()));
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUser(final HttpServletRequest request) {
    return this.ok(this.tokenSrv.getClaims(request));
  }

  @GetMapping("/user_data")
  public ResponseEntity<?> getUserData(final Principal principal) {
    return this.ok(DaoUtils.convertToDto(this.authSrv.getUserData(principal.getName()), UserInfo.class));
  }

  @PostMapping
  public ResponseEntity<?> createAccount(@RequestBody final ParamsSecurityCreateAccount params) {
    final User user = this.authSrv.createAccount(params);
    return this.ok(this.tokenSrv.createToken(user));
  }

}
