package fr.lauparr.aegir.entities.repositories;

import fr.lauparr.aegir.entities.Profile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
  @Query("select g from Profile g where g.defaultProfile = true")
  Profile findDefaultProfile();
}
