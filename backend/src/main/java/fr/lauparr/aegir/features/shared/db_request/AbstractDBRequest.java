package fr.lauparr.aegir.features.shared.db_request;

import fr.lauparr.aegir.utils.DaoUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    return this.where(this.path(field), operator, value);
  }

  private U where(Expression path, String operator, Object value) {
    this.predicates = this.andCondition(this.predicates, this.makePredicate(path, operator, value));
    return (U) this;
  }

  public U orWhere(String field, Object value) {
    return this.orWhere(field, "=", value);
  }

  public U orWhere(String field, String operator, Object value) {
    return this.orWhere(this.path(field), operator, value);
  }

  public U orWhere(Expression path, String operator, Object value) {
    this.predicates = this.orCondition(this.predicates, this.makePredicate(path, operator, value));
    return (U) this;
  }

  public U whereColumn(String path, String secondPath) {
    return this.whereColumn(path, "=", secondPath);
  }

  public U whereColumn(String path, String operator, String secondPath) {
    return this.where(path, operator, this.path(secondPath));
  }

  public U whereNull(String field) {
    this.predicates = this.andCondition(this.predicates, this.builder.isNull(this.path(field)));
    return (U) this;
  }

  public U whereNotNull(String field) {
    this.predicates = this.andCondition(this.predicates, this.builder.isNotNull(this.path(field)));
    return (U) this;
  }

  public U whereDay(String path, int day) {
    return this.whereDay(path, "=", day);
  }

  public U whereDay(String path, String operator, int year) {
    return this.where(this.builder.function("DAY", Integer.class, this.path(path)), operator, year);
  }

  public U whereMonth(String path, int day) {
    return this.whereMonth(path, "=", day);
  }

  public U whereMonth(String path, String operator, int year) {
    return this.where(this.builder.function("MONTH", Integer.class, this.path(path)), operator, year);
  }

  public U whereYear(String path, int year) {
    return this.whereYear(path, "=", year);
  }

  public U whereYear(String path, String operator, int year) {
    return this.where(this.builder.function("YEAR", Integer.class, this.path(path)), operator, year);
  }

  public U whereBetween(String field, Object first, Object last) {
    return this.whereBetween(field, this.builder.literal(first), this.builder.literal(last));
  }

  public U whereBetweenColumns(String field, String firstColumn, String lastColumn) {
    return this.whereBetween(field, this.path(firstColumn), this.path(lastColumn));
  }

  public U whereBetween(String field, Expression first, Expression last) {
    this.predicates = this.andCondition(this.predicates, this.builder.between(this.path(field), first, last));
    return (U) this;
  }

  public U whereNotBetweenColumns(String field, String firstColumn, String lastColumn) {
    return this.whereNotBetween(field, this.path(firstColumn), this.path(lastColumn));
  }

  public U whereNotBetween(String field, Object first, Object last) {
    return this.whereNotBetween(field, this.builder.literal(first), this.builder.literal(last));
  }

  public U whereNotBetween(String field, Expression first, Expression last) {
    this.predicates = this.andCondition(this.predicates, this.builder.not(this.builder.between(this.path(field), first, last)));
    return (U) this;
  }

  public U whereIn(String field, List<?> list) {
    this.predicates = this.andCondition(this.predicates, this.path(field).in(list));
    return (U) this;
  }

  public U whereIn(String field, Object... list) {
    this.predicates = this.andCondition(this.predicates, this.path(field).in(list));
    return (U) this;
  }

  public U whereNotIn(String field, List<?> list) {
    this.predicates = this.andCondition(this.predicates, this.builder.not(this.path(field).in(list)));
    return (U) this;
  }

  public U whereNotIn(String field, Object... list) {
    this.predicates = this.andCondition(this.predicates, this.builder.not(this.path(field).in(list)));
    return (U) this;
  }

  public <V> Expression<V> path(String path) {
    if (this.paths == null) {
      this.paths = new HashMap<>();
    }

    if (this.paths.containsKey(path)) {
      return this.paths.get(path);
    }

    Path<V> pathFromRoot = (Path<V>) DaoUtils.getPathFromRoot(this.root, path);
    this.paths.put(path, pathFromRoot);

    return pathFromRoot;
  }

  protected Predicate andCondition(Predicate rootPredicate, Predicate... conditions) {
    return this.addCondition(rootPredicate, true, conditions);
  }

  protected Predicate orCondition(Predicate rootPredicate, Predicate... conditions) {
    return this.addCondition(rootPredicate, false, conditions);
  }

  protected Predicate makePredicate(Expression path, String operator, Object value) {
    Predicate predicate = null;

    switch (StringUtils.lowerCase(operator)) {
      case "<>": {
        predicate = this.andCondition(predicate, this.builder.or(this.builder.notEqual(path, DaoUtils.convertValueByFieldType(path, value)), this.builder.isNull(path)));
        break;
      }
      case ">": {
        if (value instanceof Expression) {
          predicate = this.andCondition(predicate, this.builder.greaterThan(path, (Expression) value));
        } else {
          predicate = this.andCondition(predicate, this.builder.greaterThan(path, (Comparable) DaoUtils.convertValueByFieldType(path, value)));
        }
        break;
      }
      case ">=": {
        if (value instanceof Expression) {
          predicate = this.andCondition(predicate, this.builder.greaterThanOrEqualTo(path, (Expression) value));
        } else {
          predicate = this.andCondition(predicate, this.builder.greaterThanOrEqualTo(path, (Comparable) DaoUtils.convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<": {
        if (value instanceof Expression) {
          predicate = this.andCondition(predicate, this.builder.lessThan(path, (Expression) value));
        } else {
          predicate = this.andCondition(predicate, this.builder.lessThan(path, (Comparable) DaoUtils.convertValueByFieldType(path, value)));
        }
        break;
      }
      case "<=": {
        if (value instanceof Expression) {
          predicate = this.andCondition(predicate, this.builder.lessThanOrEqualTo(path, (Expression) value));
        } else {
          predicate = this.andCondition(predicate, this.builder.lessThanOrEqualTo(path, (Comparable) DaoUtils.convertValueByFieldType(path, value)));
        }
        break;
      }
      case "like": {
        predicate = this.andCondition(predicate, this.builder.like(path, DaoUtils.convertValueByFieldType(path, value).toString()));
        break;
      }
      case "unlike": {
        predicate = this.andCondition(predicate, this.builder.notLike(path, DaoUtils.convertValueByFieldType(path, value).toString()));
        break;
      }
      case "=":
      default: {
        predicate = this.andCondition(predicate, this.builder.equal(path, DaoUtils.convertValueByFieldType(path, value)));
        break;
      }
    }

    return predicate;
  }

  protected Predicate addCondition(Predicate rootPredicate, boolean and, Predicate... conditions) {
    Predicate predicate;
    if (rootPredicate == null) {
      predicate = this.builder.and(conditions);
    } else {
      if (and) {
        predicate = this.builder.and(rootPredicate, this.builder.and(conditions));
      } else {
        predicate = this.builder.or(rootPredicate, this.builder.and(conditions));
      }
    }
    return predicate;
  }

}
