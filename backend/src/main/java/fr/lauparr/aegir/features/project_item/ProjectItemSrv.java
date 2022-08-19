package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.entities.Project;
import fr.lauparr.aegir.entities.ProjectItem;
import fr.lauparr.aegir.entities.TaskStatus;
import fr.lauparr.aegir.entities.repositories.ProjectItemRepository;
import fr.lauparr.aegir.entities.repositories.ProjectRepository;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectItemSrv {

  @Autowired
  private ProjectItemRepository projectItemRepository;
  @Autowired
  private ProjectRepository projectRepository;

  public ProjectItem getItemById(Long projectItemId) {
    return this.projectItemRepository.findById(projectItemId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.project_item")));
  }

  @Transactional
  public void createProjectItem(ParamsProjectItemsCreate params) {
    ProjectItem parent = null;
    List<TaskStatus> statuses = new ArrayList<>();

    if (params.getStatuses().isEmpty()) {
      statuses.add(new TaskStatus().setName("To Do").setColor("green"));
      statuses.add(new TaskStatus().setName("In Progress").setColor("blue"));
      statuses.add(new TaskStatus().setName("To Test").setColor("orange"));
    } else {
      statuses = params.getStatuses().stream().map(param -> new TaskStatus().setName(param.getName()).setColor(param.getColor())).collect(Collectors.toList());
    }

    final Project project = projectRepository.findById(params.getProjectId()).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.project")));

    final ProjectItem projectItem = new ProjectItem()
      .setProject(project)
      .setName(params.getName())
      .setType(params.getType());

    statuses.forEach(projectItem::addStatus);

    if (params.getParentId() != null) {
      parent = getItemById(params.getParentId());
      parent.addChild(projectItem);
      projectItemRepository.save(parent);
    } else {
      project.addProjectItem(projectItem);
      projectRepository.save(project);
    }

  }
}
