package fr.lauparr.aegir.features.shared.db_request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static fr.lauparr.aegir.utils.DaoUtils.*;

public class DBRequestBuilder<T> {

  private final EntityManager em;
  private final CriteriaBuilder builder;
  private final CriteriaQuery<T> query;
  private final Root<?> root;

  private Map<String, Path> paths = new HashMap<>();

  @Getter
  private Predicate predicates = null;

  public DBRequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery<T> query, Class<?> rootClass) {
    this.em = em;
    this.builder = builder;
    this.query = query;
    this.root = query.from(rootClass);
  }

  public DBRequestBuilder<T> where(String field, Object value) {
    return this.where(field, "", value);
  }

  public DBRequestBuilder<T> where(String field, String operator, Object value) {
    return where(getPath(field), operator, value);
  }

  private DBRequestBuilder<T> where(Expression path, String operator, Object value) {
    predicates = andCondition(predicates, makePredicate(path, operator, value));
    return this;
  }

  public DBRequestBuilder<T> where(UnaryOperator<DBRequestBuilder<T>> function) {
    predicates = andCondition(null, function.apply(this).getPredicates());
    return this;
  }

  public DBRequestBuilder<T> orWhere(String field, Object value) {
    return orWhere(field, "=", value);
  }

  public DBRequestBuilder<T> orWhere(String field, String operator, Object value) {
    return orWhere(getPath(field), operator, value);
  }

  public DBRequestBuilder<T> orWhere(Expression path, String operator, Object value) {
    predicates = orCondition(predicates, makePredicate(path, operator, value));
    return this;
  }

  private Predicate makePredicate(Expression path, String operator, Object value) {
    Predicate predicate = null;

    switch (StringUtils.lowerCase(operator)) {
      case "=": {
        predicate = andCondition(predicate, builder.equal(path, convertValueByFieldType(path, value)));
        break;
      }
      case "<>": {
        predicate = andCondition(predicate, builder.or(builder.notEqual(path, convertValueByFieldType(path, value)), builder.isNull(path)));
        break;
      }
      case ">": {
        if (value instanceof Expression) {
          predicate = andCondition(predicate, builder.greaterThan(path, (Expression) value));
        } else {
          predicate = andCondition(predicate, builder.greaterThan(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case ">=": {
        if (value instanceof Expression) {
          predicate = andCondition(predicate, builder.greaterThanOrEqualTo(path, (Expression) value));
        } else {
          predicate = andCondition(predicate, builder.greaterThanOrEqualTo(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<": {
        if (value instanceof Expression) {
          predicate = andCondition(predicate, builder.lessThan(path, (Expression) value));
        } else {
          predicate = andCondition(predicate, builder.lessThan(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<=": {
        if (value instanceof Expression) {
          predicate = andCondition(predicate, builder.lessThanOrEqualTo(path, (Expression) value));
        } else {
          predicate = andCondition(predicate, builder.lessThanOrEqualTo(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "like": {
        predicate = andCondition(predicate, builder.like(path, convertValueByFieldType(path, value).toString()));
        break;
      }
      case "unlike": {
        predicate = andCondition(predicate, builder.notLike(path, convertValueByFieldType(path, value).toString()));
        break;
      }
      default: {
        predicate = andCondition(predicate, builder.equal(path, convertValueByFieldType(path, value)));
      }
    }

    return predicate;
  }


  public DBRequestBuilder<T> whereColumn(String path, String secondPath) {
    return whereColumn(path, "=", secondPath);
  }

  public DBRequestBuilder<T> whereColumn(String path, String operator, String secondPath) {
    return where(path, operator, getPath(secondPath));
  }

  public DBRequestBuilder<T> whereNull(String field) {
    predicates = andCondition(predicates, builder.isNull(getPath(field)));
    return this;
  }

  public DBRequestBuilder<T> whereDay(String path, int day) {
    return whereDay(path, "=", day);
  }

  public DBRequestBuilder<T> whereDay(String path, String operator, int year) {
    return where(builder.function("DAY", Integer.class, getPath(path)), operator, year);
  }

  public DBRequestBuilder<T> whereMonth(String path, int day) {
    return whereMonth(path, "=", day);
  }

  public DBRequestBuilder<T> whereMonth(String path, String operator, int year) {
    return where(builder.function("MONTH", Integer.class, getPath(path)), operator, year);
  }

  public DBRequestBuilder<T> whereYear(String path, int year) {
    return whereYear(path, "=", year);
  }

  public DBRequestBuilder<T> whereYear(String path, String operator, int year) {
    return where(builder.function("YEAR", Integer.class, getPath(path)), operator, year);
  }

  public DBRequestBuilder<T> whereNotNull(String field) {
    predicates = andCondition(predicates, builder.isNotNull(getPath(field)));
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    query.multiselect(Arrays.stream(fields).map(field -> getPath(field).alias(field)).toArray(Selection[]::new));
    return this;
  }

  public DBRequestBuilder<T> orderBy(String field, String order) {
    switch (order) {
      case "desc":
        query.orderBy(builder.desc(getPath(field)));
        break;
      case "asc":
      default:
        query.orderBy(builder.asc(getPath(field)));
        break;
    }
    return this;
  }

  private Predicate andCondition(Predicate rootPredicate, Predicate... conditions) {
    return addCondition(rootPredicate, true, conditions);
  }

  private Predicate orCondition(Predicate rootPredicate, Predicate... conditions) {
    return addCondition(rootPredicate, false, conditions);
  }

  public String sql() {
    return createQuery().unwrap(Query.class).getQueryString();
  }

  public List<T> list() {
    return createQuery().getResultList();
  }

  public T findFirst() {
    return createQuery().setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }

  public <U> List<U> toProjection(Class<U> projectionClass) {
    return list().stream().map(data -> convertToDto(data, projectionClass)).collect(Collectors.toList());
  }

  private TypedQuery<T> createQuery() {
    TypedQuery<T> jpaQuery;
    if (predicates == null) {
      jpaQuery = em.createQuery(query);
    } else {
      jpaQuery = em.createQuery(query.where(predicates));
    }
    return jpaQuery;
  }

  private Predicate addCondition(Predicate rootPredicate, boolean and, Predicate... conditions) {
    if (rootPredicate == null) {
      rootPredicate = builder.and(conditions);
    } else {
      if (and) {
        rootPredicate = builder.and(rootPredicate, builder.and(conditions));
      } else {
        rootPredicate = builder.or(rootPredicate, builder.and(conditions));
      }
    }
    return rootPredicate;
  }

  private <T> Path<T> getPath(String path) {
    if (paths.containsKey(path)) {
      return paths.get(path);
    }

    Path pathFromRoot = getPathFromRoot(root, path);
    paths.put(path, pathFromRoot);

    return pathFromRoot;
  }
}
