package fr.lauparr.aegir.features.project;

import fr.lauparr.aegir.entities.ProjectItem;
import fr.lauparr.aegir.entities.repositories.ProjectItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectSrv {

  @Autowired
  private ProjectItemRepository projectItemRepository;

  public List<ProjectItem> getItemsByProjectId(Long projectId) {
    return this.projectItemRepository.getByProjectId(projectId);
  }
}
