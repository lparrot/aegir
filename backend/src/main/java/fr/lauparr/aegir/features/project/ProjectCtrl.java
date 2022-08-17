package fr.lauparr.aegir.features.project;

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
@RequestMapping("api/projects")
public class ProjectCtrl extends BaseController {

  @Autowired
  private ProjectSrv service;

  @GetMapping("current_user")
  public ResponseEntity<RestApiResponse<List<ProjectInfo>>> getProjectsByCurrentUser() {
    return this.ok(DaoUtils.convertToListDto(this.service.getUserProjects(), ProjectInfo.class));
  }

  @GetMapping("{projectId}")
  public ResponseEntity<RestApiResponse<ProjectWithItemsInfo>> getProjectsById(@PathVariable("projectId") Long projectId) {
    return this.ok(DaoUtils.convertToDto(this.service.getById(projectId), ProjectWithItemsInfo.class));
  }

}
