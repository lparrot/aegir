package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Widget;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WidgetRepository extends PagingAndSortingRepository<Widget, Long>, JpaSpecificationExecutor<Widget> {
}
