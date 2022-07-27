package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
