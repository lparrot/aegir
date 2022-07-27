package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.ProjectItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectItemRepository extends PagingAndSortingRepository<ProjectItem, Long>, JpaSpecificationExecutor<ProjectItem> {
}
