package fr.lauparr.aegir.features.shared.db_request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.lauparr.aegir.utils.DaoUtils.convertValueByFieldType;
import static fr.lauparr.aegir.utils.DaoUtils.getPathFromRoot;

public abstract class AbstractDBRequest<T, U extends AbstractDBRequest<T, U>> {

  protected EntityManager em;
  protected CriteriaBuilder builder;
  protected Root<?> root;

  protected Map<String, Path> paths;

  @Getter
  protected Predicate predicates = null;

  protected AbstractDBRequest() {
    //
  }

  public U where(String field, Object value) {
    return this.where(field, "", value);
  }

  public U where(String field, String operator, Object value) {
    return where(path(field), operator, value);
  }

  private U where(Expression path, String operator, Object value) {
    predicates = andCondition(predicates, makePredicate(path, operator, value));
    return (U) this;
  }

  public U orWhere(String field, Object value) {
    return orWhere(field, "=", value);
  }

  public U orWhere(String field, String operator, Object value) {
    return orWhere(path(field), operator, value);
  }

  public U orWhere(Expression path, String operator, Object value) {
    predicates = orCondition(predicates, makePredicate(path, operator, value));
    return (U) this;
  }

  public U whereColumn(String path, String secondPath) {
    return whereColumn(path, "=", secondPath);
  }

  public U whereColumn(String path, String operator, String secondPath) {
    return where(path, operator, path(secondPath));
  }

  public U whereNull(String field) {
    predicates = andCondition(predicates, builder.isNull(path(field)));
    return (U) this;
  }

  public U whereNotNull(String field) {
    predicates = andCondition(predicates, builder.isNotNull(path(field)));
    return (U) this;
  }

  public U whereDay(String path, int day) {
    return whereDay(path, "=", day);
  }

  public U whereDay(String path, String operator, int year) {
    return where(builder.function("DAY", Integer.class, path(path)), operator, year);
  }

  public U whereMonth(String path, int day) {
    return whereMonth(path, "=", day);
  }

  public U whereMonth(String path, String operator, int year) {
    return where(builder.function("MONTH", Integer.class, path(path)), operator, year);
  }

  public U whereYear(String path, int year) {
    return whereYear(path, "=", year);
  }

  public U whereYear(String path, String operator, int year) {
    return where(builder.function("YEAR", Integer.class, path(path)), operator, year);
  }

  public U whereBetween(String field, Object first, Object last) {
    return whereBetween(field, builder.literal(first), builder.literal(last));
  }

  public U whereBetweenColumns(String field, String firstColumn, String lastColumn) {
    return whereBetween(field, path(firstColumn), path(lastColumn));
  }

  public U whereBetween(String field, Expression first, Expression last) {
    predicates = andCondition(predicates, builder.between(path(field), first, last));
    return (U) this;
  }

  public U whereNotBetweenColumns(String field, String firstColumn, String lastColumn) {
    return whereNotBetween(field, path(firstColumn), path(lastColumn));
  }

  public U whereNotBetween(String field, Object first, Object last) {
    return whereNotBetween(field, builder.literal(first), builder.literal(last));
  }

  public U whereNotBetween(String field, Expression first, Expression last) {
    predicates = andCondition(predicates, builder.not(builder.between(path(field), first, last)));
    return (U) this;
  }

  public U whereIn(String field, List<?> list) {
    predicates = andCondition(predicates, path(field).in(list));
    return (U) this;
  }

  public U whereIn(String field, Object... list) {
    predicates = andCondition(predicates, path(field).in(list));
    return (U) this;
  }

  public U whereNotIn(String field, List<?> list) {
    predicates = andCondition(predicates, builder.not(path(field).in(list)));
    return (U) this;
  }

  public U whereNotIn(String field, Object... list) {
    predicates = andCondition(predicates, builder.not(path(field).in(list)));
    return (U) this;
  }

  public <V> Expression<V> path(String path) {
    if (paths == null) {
      paths = new HashMap<>();
    }

    if (paths.containsKey(path)) {
      return paths.get(path);
    }

    Path<V> pathFromRoot = (Path<V>) getPathFromRoot(root, path);
    paths.put(path, pathFromRoot);

    return pathFromRoot;
  }

  protected Predicate andCondition(Predicate rootPredicate, Predicate... conditions) {
    return addCondition(rootPredicate, true, conditions);
  }

  protected Predicate orCondition(Predicate rootPredicate, Predicate... conditions) {
    return addCondition(rootPredicate, false, conditions);
  }

  protected Predicate makePredicate(Expression path, String operator, Object value) {
    Predicate predicate = null;

    switch (StringUtils.lowerCase(operator)) {
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
      case "=":
      default: {
        predicate = andCondition(predicate, builder.equal(path, convertValueByFieldType(path, value)));
        break;
      }
    }

    return predicate;
  }

  protected Predicate addCondition(Predicate rootPredicate, boolean and, Predicate... conditions) {
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

}
