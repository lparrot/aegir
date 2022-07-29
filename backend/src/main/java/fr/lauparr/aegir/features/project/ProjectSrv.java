package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.entities.Project;
import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.repositories.ProjectRepository;
import fr.lauparr.aegir.enums.EnumProjectItemType;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectSrv {

  @Autowired
  private ProjectRepository projectRepository;

  public List<Project> getUserProjects(User currentUser) {
    return currentUser.getProjects();
  }

  public Project getById(Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> MessageException.builder().message(MessageUtils.getMessage("error.not_foun.project")).build());
    project.setItems(project.getItems().stream().filter(projectItem -> projectItem.getType().equals(EnumProjectItemType.WORKSPACE)).collect(Collectors.toList()));
    return project;
  }
}
