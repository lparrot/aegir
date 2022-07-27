package fr.lauparr.aegir.features.project_item;

import fr.lauparr.aegir.entities.ProjectItem;
import fr.lauparr.aegir.entities.repositories.ProjectItemRepository;
import fr.lauparr.aegir.exceptions.ErrorMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectItemSrv {

  @Autowired
  private ProjectItemRepository projectItemRepository;
  
  public ProjectItem getItemById(Long projectItemId) {
    return this.projectItemRepository.findById(projectItemId).orElseThrow(() -> new ErrorMessageException("error.not_found.project_item"));
  }
}