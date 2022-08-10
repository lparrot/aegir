package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.controllers.base.BaseController;
import fr.lauparr.aegir.dto.GroupByMapper;
import fr.lauparr.aegir.dto.api.RestApiResponse;
import fr.lauparr.aegir.entities.Task;
import fr.lauparr.aegir.features.project_item.ProjectItemInfo;
import fr.lauparr.aegir.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskCtrl extends BaseController {

  @Autowired
  private TaskSrv taskSrv;

  @GetMapping("/project_items/{projectItemId}")
  public HttpEntity<RestApiResponse<List<GroupByMapper<ProjectItemInfo, GroupByMapper<TaskStatusInfo, TaskInfo>>>>> getTasksByProjectItemId(@PathVariable("projectItemId") Long projectItemId) {
    List<Task> tasks = taskSrv.getTasksByProjectItemId(projectItemId);

    return this.ok(taskSrv.groupTasksByViews(tasks).stream().map(mapperView -> mapperView.convert(
      master -> DaoUtils.convertToDto(master, ProjectItemInfo.class),
      details -> taskSrv.groupTasksByStatus(details).stream().map(mapperStatus -> mapperStatus.convert(
        masterStatus -> DaoUtils.convertToDto(masterStatus, TaskStatusInfo.class),
        detailsStatus -> DaoUtils.convertListDto(detailsStatus, TaskInfo.class)
      )).collect(Collectors.toList())
    )).collect(Collectors.toList()));
  }

}
