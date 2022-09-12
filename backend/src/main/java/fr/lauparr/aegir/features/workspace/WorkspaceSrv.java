package fr.lauparr.aegir.features.workspace;

import fr.lauparr.aegir.entities.User;
import fr.lauparr.aegir.entities.Workspace;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.repositories.WorkspaceRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import fr.lauparr.aegir.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceSrv {

  @Autowired
  private WorkspaceRepository workspaceRepository;

  public List<Workspace> getUserWorkspaces() {
    User user = SpringUtils.getCurrentUser();
    return workspaceRepository.findAllWorkspaceByUserId(user.getId());
  }

  public Workspace getById(Long workspaceId) {
    return workspaceRepository.findById(workspaceId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.workspace")));
  }
}
