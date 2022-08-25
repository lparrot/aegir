package fr.lauparr.aegir.features.user;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserCtrl extends BaseController {

  @Autowired
  private UserSrv userSrv;

  @GetMapping
  public ResponseEntity<RestApiResponse<List<UserDto>>> getUsers() {
    return this.ok(DaoUtils.mapToListDto(userSrv.getUsers(), UserDto.class));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<RestApiResponse<UserDto>> getUserById(@PathVariable("userId") Long userId) {
    return this.ok(DaoUtils.mapToDto(userSrv.getUserById(userId), UserDto.class));
  }

}
