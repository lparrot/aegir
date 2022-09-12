package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Notification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
}
