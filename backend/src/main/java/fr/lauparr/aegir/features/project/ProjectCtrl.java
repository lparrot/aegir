package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.features.project_item.ProjectItemInfo;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects")
public class ProjectCtrl extends BaseController {

  @Autowired
  private ProjectSrv service;

  @GetMapping("{projectId}/items")
  public ResponseEntity<?> getItemsByProjectId(@PathVariable("projectId") Long projectId) {
    return this.ok(DaoUtils.convertListDto(this.service.getItemsByProjectId(projectId), ProjectItemInfo.class));
  }

}
