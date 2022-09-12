package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskCtrl extends BaseController {

  @Autowired
  private TaskSrv taskSrv;

  @GetMapping("/{taskId}/details")
  public ResponseEntity<RestApiResponse<TaskDto_Detail>> getTaskDetails(@PathVariable("taskId") Long taskId) {
    return this.ok(DaoUtils.mapToDto(taskSrv.getTaskById(taskId), TaskDto_Detail.class));
  }

  @PostMapping("/{taskId}/comments")
  public ResponseEntity<RestApiResponse<TaskDto_Detail.TaskCommentDto>> postAddComment(@PathVariable("taskId") Long taskId, @RequestBody ParamsTaskPostAddComment params) {
    return this.ok(DaoUtils.mapToDto(taskSrv.addComment(taskId, params), TaskDto_Detail.TaskCommentDto.class));
  }

}
