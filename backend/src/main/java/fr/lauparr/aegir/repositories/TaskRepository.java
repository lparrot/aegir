package fr.lauparr.aegir.repositories;

import fr.lauparr.aegir.entities.Task;
import fr.lauparr.aegir.repositories.abstracts.SoftDeletableRepository;

public interface TaskRepository extends SoftDeletableRepository<Task, Long> {
}
