package fr.lauparr.aegir.entities.abstracts;

import fr.lauparr.aegir.config.AutowireHelper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class JpaModel<ID> implements Serializable {

  private static Class clazz;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected ID id;

  public JpaModel() {
    clazz = this.getClass();
  }

  public static <T> List<T> all() {
    return getEm().createQuery(String.format("select o from %s o", clazz.getSimpleName())).getResultList();
  }

  public static EntityManager getEm() {
    return AutowireHelper.getBean(EntityManager.class);
  }

}
