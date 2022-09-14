package fr.lauparr.aegir.features.board;

import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.projections.BoardInfo_Detail;
import fr.lauparr.aegir.repositories.BoardRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardSrv {

  @Autowired
  private BoardRepository boardRepository;

  public BoardInfo_Detail getBoardById(Long id) {
    return boardRepository.findBoardDetailById(id, BoardInfo_Detail.class).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.board")));
  }
}
