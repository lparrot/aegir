package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/project_items")
public class ProjectItemCtrl {

  @Autowired
  private ProjectItemSrv service;

  @GetMapping("{projectItemId}")
  public ResponseEntity<?> getItemsByProjectId(@PathVariable("projectItemId") Long projectItemId) {
    return ResponseEntity.ok(DaoUtils.convertToDto(this.service.getItemById(projectItemId), ProjectItemInfo.class));
  }

}
