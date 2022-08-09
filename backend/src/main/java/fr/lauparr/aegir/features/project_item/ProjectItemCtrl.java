package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/project_items")
public class ProjectItemCtrl extends BaseController {

  @Autowired
  private ProjectItemSrv service;

  @GetMapping("{projectItemId}")
  public ResponseEntity<RestApiResponse<ProjectItemInfo>> getProjectItemsById(@PathVariable("projectItemId") Long projectItemId) {
    return this.ok(DaoUtils.convertToDto(this.service.getItemById(projectItemId), ProjectItemInfo.class));
  }

}
