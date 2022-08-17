package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.GroupByMapper;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.entities.Task;
import fr.lauparr.aegir.features.project_item.ProjectItemInfo;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskCtrl extends BaseController {

  @Autowired
  private TaskSrv taskSrv;

  @GetMapping("/project_items/{projectItemId}")
  public ResponseEntity<RestApiResponse<List<GroupByMapper<ProjectItemInfo, GroupByMapper<TaskStatusInfo, TaskInfo>>>>> getTasksByProjectItemId(@PathVariable("projectItemId") Long projectItemId) {
    List<Task> tasks = taskSrv.getTasksByProjectItemId(projectItemId);

    return this.ok(taskSrv.groupTasksByViews(tasks).stream().map(mapperView -> mapperView.convert(
      master -> DaoUtils.convertToDto(master, ProjectItemInfo.class),
      details -> taskSrv.groupTasksByStatus(details).stream().map(mapperStatus -> mapperStatus.convert(
        masterStatus -> DaoUtils.convertToDto(masterStatus, TaskStatusInfo.class),
        detailsStatus -> DaoUtils.convertToListDto(detailsStatus, TaskInfo.class)
      )).collect(Collectors.toList())
    )).collect(Collectors.toList()));
  }

  @GetMapping("/{taskId}/details")
  public ResponseEntity<RestApiResponse<TaskDetailDto>> getTaskDetails(@PathVariable("taskId") Long taskId) {
    return this.ok(DaoUtils.mapToDto(taskSrv.getTaskById(taskId), TaskDetailDto.class));
  }

  @PostMapping("/{taskId}/comments")
  public ResponseEntity<RestApiResponse<TaskDetailDto.TaskCommentDto>> postAddComment(@PathVariable("taskId") Long taskId, @RequestBody ParamsTaskPostAddComment params) {
    return this.ok(DaoUtils.mapToDto(taskSrv.addComment(taskId, params), TaskDetailDto.TaskCommentDto.class));
  }

}
