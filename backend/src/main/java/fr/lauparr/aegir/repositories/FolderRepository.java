package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Folder;
import fr.lauparr.aegir.repositories.abstracts.SoftDeletableRepository;

public interface FolderRepository extends SoftDeletableRepository<Folder, Long> {
}
