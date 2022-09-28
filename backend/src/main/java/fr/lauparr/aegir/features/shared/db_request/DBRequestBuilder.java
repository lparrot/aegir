package fr.lauparr.aegir.features.shared.db_request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static fr.lauparr.aegir.utils.DaoUtils.*;

public class DBRequestBuilder<T> {

  private final EntityManager em;
  private final CriteriaBuilder builder;
  private final CriteriaQuery<T> query;
  private final Root<?> root;

  private final Map<String, Path> paths;

  @Getter
  private Predicate predicates = null;

  public DBRequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery<T> query, Root<?> root) {
    this(em, builder, query, root, null);
  }

  public DBRequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery<T> query, Root<?> root, Map<String, Path> paths) {
    this.em = em;
    this.builder = builder;
    this.query = query;
    this.root = root;
    this.paths = paths == null ? new HashMap<>() : paths;
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
    predicates = andCondition(predicates, andCondition(function.apply(new DBRequestBuilder<>(em, builder, query, root, paths)).getPredicates()));
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

  public DBRequestBuilder<T> orWhere(UnaryOperator<DBRequestBuilder<T>> function) {
    predicates = orCondition(predicates, andCondition(function.apply(new DBRequestBuilder<>(em, builder, query, root, paths)).getPredicates()));
    return this;
  }

  public DBRequestBuilder<T> whereNot(UnaryOperator<DBRequestBuilder<T>> function) {
    predicates = andCondition(predicates, builder.not(function.apply(new DBRequestBuilder<>(em, builder, query, root, paths)).getPredicates()));
    return this;
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

  public DBRequestBuilder<T> whereNotNull(String field) {
    predicates = andCondition(predicates, builder.isNotNull(getPath(field)));
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

  public DBRequestBuilder<T> whereBetween(String field, Object first, Object last) {
    return whereBetween(field, builder.literal(first), builder.literal(last));
  }

  public DBRequestBuilder<T> whereBetweenColumns(String field, String firstColumn, String lastColumn) {
    return whereBetween(field, getPath(firstColumn), getPath(lastColumn));
  }

  public DBRequestBuilder<T> whereBetween(String field, Expression first, Expression last) {
    predicates = andCondition(predicates, builder.between(getPath(field), first, last));
    return this;
  }

  public DBRequestBuilder<T> whereNotBetweenColumns(String field, String firstColumn, String lastColumn) {
    return whereNotBetween(field, getPath(firstColumn), getPath(lastColumn));
  }

  public DBRequestBuilder<T> whereNotBetween(String field, Object first, Object last) {
    return whereNotBetween(field, builder.literal(first), builder.literal(last));
  }

  public DBRequestBuilder<T> whereNotBetween(String field, Expression first, Expression last) {
    predicates = andCondition(predicates, builder.not(builder.between(getPath(field), first, last)));
    return this;
  }

  public DBRequestBuilder<T> whereIn(String field, List<?> list) {
    predicates = andCondition(predicates, getPath(field).in(list));
    return this;
  }

  public DBRequestBuilder<T> whereIn(String field, Object... list) {
    predicates = andCondition(predicates, getPath(field).in(list));
    return this;
  }

  public DBRequestBuilder<T> whereNotIn(String field, List<?> list) {
    predicates = andCondition(predicates, builder.not(getPath(field).in(list)));
    return this;
  }

  public DBRequestBuilder<T> whereNotIn(String field, Object... list) {
    predicates = andCondition(predicates, builder.not(getPath(field).in(list)));
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    query.multiselect(Arrays.stream(fields).map(field -> getPath(field).alias(field)).toArray(Selection[]::new));
    return this;
  }

  public DBRequestBuilder<T> orderBy(String field, String order) {
    ArrayList<Order> orders = new ArrayList<>(query.getOrderList());
    switch (order) {
      case "desc":
        orders.add(builder.desc(getPath(field)));
        break;
      case "asc":
      default:
        orders.add(builder.asc(getPath(field)));
        break;
    }

    query.orderBy(orders);

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

  public T first() {
    return createQuery().setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }

  public <U> List<U> projection(Class<U> projectionClass) {
    return list().stream().map(data -> convertToDto(data, projectionClass)).collect(Collectors.toList());
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
    Predicate predicate;
    if (rootPredicate == null) {
      predicate = builder.and(conditions);
    } else {
      if (and) {
        predicate = builder.and(rootPredicate, builder.and(conditions));
      } else {
        predicate = builder.or(rootPredicate, builder.and(conditions));
      }
    }
    return predicate;
  }

  private <U> Expression<U> getPath(String path) {
    if (paths.containsKey(path)) {
      return paths.get(path);
    }

    Path<U> pathFromRoot = (Path<U>) getPathFromRoot(root, path);
    paths.put(path, pathFromRoot);

    return pathFromRoot;
  }
}
