package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project> {

  @Query("select p from Project p left join p.items i on (i.parent is null) where p.id = :projectId")
  Optional<Project> getProjectByIdAndType(@Param("projectId") Long projectId);

  @Query("select p from Project p left join p.members m where m.user.id = :userId")
  List<Project> findAllUserProjectsByUserId(@Param("userId") Long userId);
}
