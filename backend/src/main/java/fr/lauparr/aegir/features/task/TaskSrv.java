package fr.lauparr.aegir.features.task;

import fr.lauparr.aegir.dto.GroupByMapper;
import fr.lauparr.aegir.entities.Task;
import fr.lauparr.aegir.entities.TaskComment;
import fr.lauparr.aegir.entities.TaskStatus;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.repositories.TaskRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import fr.lauparr.aegir.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskSrv {

  @Autowired
  private TaskRepository taskRepository;

  public List<GroupByMapper<TaskStatus, Task>> groupTasksByStatus(List<Task> tasks) {
    Map<TaskStatus, List<Task>> collector = tasks.stream().collect(Collectors.groupingBy(Task::getStatus));
    return collector.entrySet().stream().map(entry -> new GroupByMapper<>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
  }

  public Task getTaskById(Long taskId) {
    return taskRepository.findById(taskId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.task")));
  }

  public TaskComment addComment(Long taskId, ParamsTaskPostAddComment params) {
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.task")));
    final TaskComment taskComment = new TaskComment().setUser(SpringUtils.getCurrentUser()).setContent(params.getContent()).setCreatedAt(LocalDateTime.now());
    task.addComment(taskComment);

    taskRepository.save(task);

    return taskComment;
  }
}
