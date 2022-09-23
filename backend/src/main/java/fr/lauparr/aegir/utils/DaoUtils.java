package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.config.AutowireHelper;
import lombok.experimental.UtilityClass;
import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DaoUtils {

  private final String SPLIT_CHAR = "\\.";

  public <T> T convertToDto(final Object data, final Class<T> entityClass) {
    if (data == null) {
      return null;
    }
    return AutowireHelper.getBean(ProjectionFactory.class).createProjection(entityClass, data);
  }

  public <T> List<T> convertToListDto(final List<?> liste, final Class<T> entityClass) {
    if (liste == null) {
      return new ArrayList<>();
    }
    return liste.stream().map(x -> DaoUtils.convertToDto(x, entityClass)).collect(Collectors.toList());
  }

  public <T> Page<T> convertToPageDto(final Page<?> page, final Class<T> entityClass) {
    if (page == null) {
      return Page.empty();
    }
    return page.map(x -> DaoUtils.convertToDto(x, entityClass));
  }

  public <T> T mapToDto(final Object data, final Class<T> entityClass) {
    if (data == null) {
      return null;
    }
    return AutowireHelper.getBean(ModelMapper.class).map(data, entityClass);
  }

  public <T> List<T> mapToListDto(final List<?> liste, final Class<T> entityClass) {
    if (liste == null) {
      return new ArrayList<>();
    }
    return liste.stream().map(x -> DaoUtils.mapToDto(x, entityClass)).collect(Collectors.toList());
  }

  public <T> Page<T> mapToPageDto(final Page<?> page, final Class<T> entityClass) {
    if (page == null) {
      return Page.empty();
    }
    return page.map(x -> DaoUtils.mapToDto(x, entityClass));
  }

  public Cache getCache() {
    return AutowireHelper.getBean(SessionFactory.class).getCache();
  }

  public <T> void evictRelationCache(Class<T> clazz, String field) {
    getCache().evictRegion(String.format("%s.%s", clazz.getName(), field));
  }

  public <T> T findRandom(final PagingAndSortingRepository<T, ?> repository) {
    final long count = repository.count();
    final int idx = new SecureRandom().nextInt((int) count);
    final List<T> result = repository.findAll(PageRequest.of(idx, 1)).getContent();
    if (!result.isEmpty()) {
      return result.get(0);
    }
    return null;
  }

  /**
   * Récupère le Path à partir d'un Root
   */
  public <T> Path<T> getPathFromRoot(final Root<T> root, final String field) {
    final String principal;
    String[] fields = null;
    if (field != null) {
      final String[] checks = field.split(DaoUtils.SPLIT_CHAR);
      principal = checks[0];
      if (checks.length > 1) {
        fields = Arrays.copyOfRange(checks, 1, checks.length);
      }
    } else {
      principal = null;
    }
    final Path<T> p;
    if (DaoUtils.isSubPath(fields)) {
      p = DaoUtils.crossPathToPath(root.join(principal, JoinType.LEFT), fields);
    } else {
      p = root.get(principal);
    }
    return p;
  }

  public Object convertValueByFieldType(Expression<?> path, Object value) {
    final Class<?> type = path.getJavaType();

    if (value == null) {
      return null;
    }

    if (value.getClass().equals(String.class)) {

      // Modification de la valeur à rechercher par rapport à la classe du champ
      if (type.equals(LocalDate.class)) {
        return DateTimeUtils.convertToLocalDate((String) value);
      }

      if (type.equals(LocalDateTime.class)) {
        return DateTimeUtils.convertToLocalDateTime((String) value);
      }

      if (type.equals(Boolean.class) || type.equals(boolean.class)) {
        return Boolean.valueOf((String) value);
      }

    }

    return value;
  }

  /**
   * Vérifie qu'il ne s'agit pas du dernier Path "extractible"
   */
  private boolean isSubPath(final String[] fields) {
    return fields != null && fields.length > 0;
  }

  /**
   * Récupère un Path à partir du Path et de la liste de champs filtrés
   */
  private <T> Path<T> crossPathToPath(final Path<T> path, final String[] fields) {
    Path<T> lastPath = path;
    if (fields != null) {
      for (final String key : fields) {
        lastPath = lastPath.get(key);
      }
    }
    return lastPath;
  }


}
