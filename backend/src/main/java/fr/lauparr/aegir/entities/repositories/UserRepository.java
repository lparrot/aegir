package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findFirstByUsername(String username);
}
