package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Board;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardRepository extends PagingAndSortingRepository<Board, Long>, JpaSpecificationExecutor<Board> {
}
