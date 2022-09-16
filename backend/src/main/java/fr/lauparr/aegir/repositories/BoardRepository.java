package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Board;
import fr.lauparr.aegir.repositories.abstracts.SoftDeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends SoftDeletableRepository<Board, Long> {

  @Query("select b from Board b where b.id = :id")
  <T> Optional<T> findBoardDetailById(@Param("id") Long id, Class<T> type);

}
