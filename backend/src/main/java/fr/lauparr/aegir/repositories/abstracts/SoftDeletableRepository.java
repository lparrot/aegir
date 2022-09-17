package fr.lauparr.aegir.repositories.abstracts;

import fr.lauparr.aegir.entities.abstracts.SoftDeletableEntity;
import fr.lauparr.aegir.exceptions.MessageException;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface SoftDeletableRepository<T extends SoftDeletableEntity, ID> extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

  default void softDelete(T entity) {
    Assert.notNull(entity, "The entity must not be null!");
    Assert.isInstanceOf(SoftDeletableEntity.class, entity, "The entity must be soft deletable!");
    entity.setDeletedAt(LocalDateTime.now());
    save(entity);
  }

  default void softDeleteById(ID id) {
    Assert.notNull(id, "The given id must not be null!");
    this.softDelete(findById(id).orElseThrow(() -> new MessageException(String.format("No %s entity with id %s exists!", "", id))));
  }

  @Query("update #{#entityName} e set e.deletedAt = current_date  where e.id in :ids")
  void softDeleteByIds(@Param("ids") List<ID> ids);

}
