package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.entities.Project;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.repositories.ProjectRepository;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import fr.lauparr.aegir.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectSrv {

  @Autowired
  private ProjectRepository projectRepository;

  public List<Project> getUserProjects() {
    User user = SpringUtils.getCurrentUser();
    return projectRepository.findAllUserProjectsByUserId(user.getId());
  }

  public Project getById(Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.project")));
    project.setItems(project.getItems().stream().filter(projectItem -> projectItem.getType().equals(EnumProjectItemType.WORKSPACE)).collect(Collectors.toList()));
    return project;
  }
}
