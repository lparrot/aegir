package fr.lauparr.aegir.features.board;

import fr.lauparr.aegir.entities.Board;
import fr.lauparr.aegir.entities.Folder;
import fr.lauparr.aegir.entities.Task;
import fr.lauparr.aegir.entities.Workspace;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.projections.BoardInfo_Detail;
import fr.lauparr.aegir.repositories.BoardRepository;
import fr.lauparr.aegir.repositories.FolderRepository;
import fr.lauparr.aegir.repositories.TaskRepository;
import fr.lauparr.aegir.repositories.WorkspaceRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardSrv {

  @Autowired
  private BoardRepository boardRepository;
  @Autowired
  private WorkspaceRepository workspaceRepository;
  @Autowired
  private FolderRepository folderRepository;
  @Autowired
  private TaskRepository taskRepository;

  public BoardInfo_Detail getBoardById(Long id) {
    return boardRepository.findBoardDetailById(id, BoardInfo_Detail.class).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.board")));
  }

  @Transactional
  public void createBoard(Long workspaceId, ParamsCreateBoard params) {
    Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.workspace")));

    Board board = new Board()
      .setWorkspace(workspace)
      .setName(params.getName())
      .setDescription(params.getDescription());

    workspace.getBoards().add(board);

    boardRepository.save(board);
  }

  @Transactional
  public void createFolder(Long workspaceId, ParamsCreateFolder params) {
    Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.workspace")));

    Folder folder = new Folder()
      .setWorkspace(workspace)
      .setName(params.getName());

    workspace.getFolders().add(folder);

    folderRepository.save(folder);
  }

  public void deleteBoard(Long boardId, boolean cascade) {
    Board board = boardRepository.findById(boardId).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.board")));

    boardRepository.softDelete(board);
    if (cascade) {
      List<Long> ids = board.getTasks().stream().map(Task::getId).collect(Collectors.toList());
      taskRepository.softDeleteByIds(ids);
    }
  }
}
