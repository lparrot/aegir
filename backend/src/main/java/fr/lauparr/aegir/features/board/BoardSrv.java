package fr.lauparr.aegir.features.board;

import fr.lauparr.aegir.entities.Board;
import fr.lauparr.aegir.exceptions.MessageException;
import fr.lauparr.aegir.repositories.BoardRepository;
import fr.lauparr.aegir.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardSrv {

  @Autowired
  private BoardRepository boardRepository;

  public Board getBoardById(Long id) {
    return boardRepository.findById(id).orElseThrow(() -> new MessageException(MessageUtils.getMessage("message.error.not_found.board")));
  }
}
