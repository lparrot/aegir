package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.dto.GroupByMapper;
import fr.lauparr.aegir.entities.*;
import fr.lauparr.aegir.entities.repositories.ProjectItemRepository;
import fr.lauparr.aegir.entities.repositories.TaskRepository;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskSrv {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private ProjectItemRepository projectItemRepository;

  public List<Task> getTasksByProjectItemId(Long projectItemId) {
    ProjectItem item = projectItemRepository.findById(projectItemId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.project_item")));

    List<Long> ids = new ArrayList<>();
    ids.add(item.getId());
    ids.addAll(item.getAllChildren().stream().map(ProjectItem::getId).collect(Collectors.toList()));

    return taskRepository.findAll((root, query, criteriaBuilder) -> root.get(Task_.view).get(ProjectItem_.id).in(ids));
  }

  public List<GroupByMapper<ProjectItem, Task>> groupTasksByViews(List<Task> tasks) {
    Map<ProjectItem, List<Task>> collector = tasks.stream().collect(Collectors.groupingBy(Task::getView));
    return collector.entrySet().stream().map(entry -> new GroupByMapper<>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
  }

  public List<GroupByMapper<TaskStatus, Task>> groupTasksByStatus(List<Task> tasks) {
    Map<TaskStatus, List<Task>> collector = tasks.stream().collect(Collectors.groupingBy(Task::getStatus));
    return collector.entrySet().stream().map(entry -> new GroupByMapper<>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
  }

}
