package fr.lauparr.aegir.features.user;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.projections.UserInfo_Datatable;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserCtrl extends BaseController {

  @Autowired
  private UserSrv userSrv;

  @GetMapping
  public ResponseEntity<RestApiResponse<List<UserInfo_Datatable>>> getUsers() {
    return this.ok(DaoUtils.convertToListDto(userSrv.getUsers(), UserInfo_Datatable.class));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<RestApiResponse<UserInfo_Datatable>> getUserById(@PathVariable("userId") Long userId) {
    return this.ok(DaoUtils.convertToDto(userSrv.getUserById(userId), UserInfo_Datatable.class));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<RestApiResponse<Void>> updateUser(@PathVariable("userId") Long userId, @RequestBody ParamsUserEdit params) {
    this.userSrv.updateUser(userId, params);
    return this.ok();
  }

}
