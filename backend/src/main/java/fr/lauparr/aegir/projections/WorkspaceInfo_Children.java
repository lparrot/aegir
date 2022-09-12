package fr.lauparr.aegir.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * A Projection for the {@link fr.lauparr.aegir.entities.Workspace} entity
 */
public interface WorkspaceInfo_Children {
  Long getId();

  String getName();

  @Value("#{target.getBoardsNotInFolder()}")
  List<BoardInfo_Simple> getBoards();

  List<FolderInfo_Children> getFolders();
}
