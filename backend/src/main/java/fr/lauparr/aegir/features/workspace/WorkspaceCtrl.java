package fr.lauparr.aegir.features.workspace;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.projections.WorkspaceInfo_Children;
import fr.lauparr.aegir.projections.WorkspaceInfo_Simple;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/workspaces")
public class WorkspaceCtrl extends BaseController {

  @Autowired
  private WorkspaceSrv service;

  @GetMapping("current_user")
  public ResponseEntity<RestApiResponse<List<WorkspaceInfo_Simple>>> getWorkspacesByCurrentUser() {
    return this.ok(DaoUtils.convertToListDto(this.service.getUserWorkspaces(), WorkspaceInfo_Simple.class));
  }

  @GetMapping("current_user/children")
  public ResponseEntity<RestApiResponse<List<WorkspaceInfo_Children>>> getWorkspacesWithChildrenByCurrentUser() {
    return this.ok(DaoUtils.convertToListDto(this.service.getUserWorkspaces(), WorkspaceInfo_Children.class));
  }

  @GetMapping("{workspaceId}")
  public ResponseEntity<RestApiResponse<WorkspaceInfo_Children>> getWorkspaceById(@PathVariable("workspaceId") Long workspaceId) {
    return this.ok(DaoUtils.convertToDto(this.service.getById(workspaceId), WorkspaceInfo_Children.class));
  }

}
