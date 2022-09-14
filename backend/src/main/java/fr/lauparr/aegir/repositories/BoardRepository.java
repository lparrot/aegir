package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Board;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends PagingAndSortingRepository<Board, Long>, JpaSpecificationExecutor<Board> {

  @Query("select b from Board b where b.id = :id")
  <T> Optional<T> findBoardDetailById(@Param("id") Long id, Class<T> type);

}
