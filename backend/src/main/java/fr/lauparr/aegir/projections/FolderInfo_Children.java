package fr.lauparr.aegir.projections;

import java.util.List;

/**
 * A Projection for the {@link fr.lauparr.aegir.entities.Folder} entity
 */
public interface FolderInfo_Children {
  Long getId();

  String getName();

  List<BoardInfo_Simple> getBoards();
}
