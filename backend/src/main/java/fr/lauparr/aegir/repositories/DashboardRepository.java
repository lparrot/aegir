package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Dashboard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DashboardRepository extends PagingAndSortingRepository<Dashboard, Long>, JpaSpecificationExecutor<Dashboard> {
}
