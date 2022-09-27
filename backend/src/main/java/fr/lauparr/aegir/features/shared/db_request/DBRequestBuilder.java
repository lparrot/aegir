package fr.lauparr.aegir.features.shared.db_request;

import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fr.lauparr.aegir.utils.DaoUtils.*;

public class DBRequestBuilder<T> {

  private final EntityManager em;
  private final CriteriaBuilder builder;
  private final CriteriaQuery<T> query;
  private final Root<?> root;

  List<Predicate> predicates = new ArrayList<>();

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
    return where(getPathFromRoot(root, field), operator, value);
  }

  public DBRequestBuilder<T> where(Expression path, String operator, Object value) {
    switch (operator) {
      case "=": {
        predicates.add(builder.equal(path, convertValueByFieldType(path, value)));
        break;
      }
      case "<>": {
        predicates.add(builder.or(builder.notEqual(path, convertValueByFieldType(path, value)), builder.isNull(path)));
        break;
      }
      case ">": {
        if (value instanceof Expression) {
          predicates.add(builder.greaterThan(path, (Expression) value));
        } else {
          predicates.add(builder.greaterThan(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case ">=": {
        if (value instanceof Expression) {
          predicates.add(builder.greaterThanOrEqualTo(path, (Expression) value));
        } else {
          predicates.add(builder.greaterThanOrEqualTo(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<": {
        if (value instanceof Expression) {
          predicates.add(builder.lessThan(path, (Expression) value));
        } else {
          predicates.add(builder.lessThan(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<=": {
        if (value instanceof Expression) {
          predicates.add(builder.lessThanOrEqualTo(path, (Expression) value));
        } else {
          predicates.add(builder.lessThanOrEqualTo(path, (Comparable) convertValueByFieldType(path, value)));
        }
        break;
      }
      case "like": {
        predicates.add(builder.like(path, convertValueByFieldType(path, value).toString()));
        break;
      }
      case "unlike": {
        predicates.add(builder.notLike(path, convertValueByFieldType(path, value).toString()));
        break;
      }
      default: {
        predicates.add(builder.equal(path, convertValueByFieldType(path, value)));
      }
    }
    return this;
  }

  public DBRequestBuilder<T> whereColumn(String path, String secondPath) {
    return whereColumn(path, "=", secondPath);
  }

  public DBRequestBuilder<T> whereColumn(String path, String operator, String secondPath) {
    return where(path, operator, getPathFromRoot(root, secondPath));
  }

  public DBRequestBuilder<T> whereNull(String field) {
    predicates.add(builder.isNull(getPathFromRoot(root, field)));
    return this;
  }

  public DBRequestBuilder<T> whereDay(String path, int day) {
    return whereDay(path, "=", day);
  }

  public DBRequestBuilder<T> whereDay(String path, String operator, int year) {
    return where(builder.function("DAY", Integer.class, getPathFromRoot(root, path)), operator, year);
  }

  public DBRequestBuilder<T> whereMonth(String path, int day) {
    return whereMonth(path, "=", day);
  }

  public DBRequestBuilder<T> whereMonth(String path, String operator, int year) {
    return where(builder.function("MONTH", Integer.class, getPathFromRoot(root, path)), operator, year);
  }

  public DBRequestBuilder<T> whereYear(String path, int year) {
    return whereYear(path, "=", year);
  }

  public DBRequestBuilder<T> whereYear(String path, String operator, int year) {
    return where(builder.function("YEAR", Integer.class, getPathFromRoot(root, path)), operator, year);
  }

  public DBRequestBuilder<T> whereNotNull(String field) {
    predicates.add(builder.isNotNull(getPathFromRoot(root, field)));
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    query.multiselect(Arrays.stream(fields).map(field -> getPathFromRoot(root, field).alias(field)).toArray(Selection[]::new));
    return this;
  }

  public DBRequestBuilder<T> orderBy(String field, String order) {
    switch (order) {
      case "desc":
        query.orderBy(builder.desc(getPathFromRoot(root, field)));
        break;
      case "asc":
      default:
        query.orderBy(builder.asc(getPathFromRoot(root, field)));
        break;
    }
    return this;
  }

  public String sql() {
    return em.createQuery(query.where(predicates.toArray(new Predicate[0]))).unwrap(Query.class).getQueryString();
  }

  public List<T> list() {
    return em.createQuery(query.where(predicates.toArray(new Predicate[0]))).getResultList();
  }

  public T findFirst() {
    return em.createQuery(query.where(predicates.toArray(new Predicate[0]))).setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }

  public <U> List<U> toProjection(Class<U> projectionClass) {
    return list().stream().map(data -> convertToDto(data, projectionClass)).collect(Collectors.toList());
  }
}
