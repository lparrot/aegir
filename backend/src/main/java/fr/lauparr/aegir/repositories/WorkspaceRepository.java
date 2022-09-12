package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Workspace;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends PagingAndSortingRepository<Workspace, Long>, JpaSpecificationExecutor<Workspace> {
  @Query("select w from Workspace w left join w.members m where m.user.id = :userId")
  List<Workspace> findAllWorkspaceByUserId(@Param("userId") Long userId);
}
