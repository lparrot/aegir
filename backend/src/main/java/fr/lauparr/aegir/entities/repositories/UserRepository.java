package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findFirstByUsername(String username);

  default Optional<User> getSessionUser() {
    if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getName() == null) {
      return Optional.empty();
    }

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
    return findFirstByUsername(username);
  }
}
