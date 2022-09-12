package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.UserData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDataRepository extends PagingAndSortingRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {
}
