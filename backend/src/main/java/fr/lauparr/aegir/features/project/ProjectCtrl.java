package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects")
public class ProjectCtrl extends BaseController {

  @Autowired
  private ProjectSrv service;

  @GetMapping("current_user")
  public ResponseEntity<?> getUserProjects(@AuthenticationPrincipal User currentUser) {
    return this.ok(DaoUtils.convertListDto(this.service.getUserProjects(currentUser), ProjectInfo.class));
  }

  @GetMapping("{projectId}")
  public ResponseEntity<?> getById(@PathVariable("projectId") Long projectId) {
    return this.ok(DaoUtils.convertToDto(this.service.getById(projectId), ProjectWithItemsInfo.class));
  }

}
