package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Folder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FolderRepository extends PagingAndSortingRepository<Folder, Long>, JpaSpecificationExecutor<Folder> {
}
