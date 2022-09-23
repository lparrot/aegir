package fr.lauparr.aegir.features.shared.services.db_request;

import fr.lauparr.aegir.utils.DaoUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBRequestBuilder<T> {

  private final EntityManager em;
  private final CriteriaBuilder builder;
  private final CriteriaQuery<T> query;
  private final Root<T> root;

  List<Predicate> predicates = new ArrayList<>();

  public DBRequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery<T> query, Class rootClass) {
    this.em = em;
    this.builder = builder;
    this.query = query;
    this.root = query.from(rootClass);
  }

  public DBRequestBuilder<T> where(String field, Object value) {
    return this.where(field, "", value);
  }

  public <U extends Comparable> DBRequestBuilder<T> where(String field, String operator, Object value) {
    final Expression path = DaoUtils.getPathFromRoot(root, field);

    switch (operator) {
      case "=": {
        predicates.add(builder.equal(path, DaoUtils.convertValueByFieldType(path, value)));
        break;
      }
      case "<>": {
        predicates.add(builder.or(builder.notEqual(path, value), builder.isNull(path)));
        break;
      }
      case ">": {
        predicates.add(builder.greaterThan(path, (U) DaoUtils.convertValueByFieldType(path, value)));
        break;
      }
      case "like": {
        predicates.add(builder.like((Expression<String>) path, value.toString()));
        break;
      }
      case "unlike": {
        predicates.add(builder.notLike((Expression<String>) path, value.toString()));
        break;
      }
      default: {
        predicates.add(builder.equal(path, value));
      }
    }
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    query.multiselect(Arrays.stream(fields).map(field -> DaoUtils.getPathFromRoot(root, field).alias(field)).toArray(Path[]::new));
    return this;
  }

  public List<T> list() {
    return em.createQuery(query.where(predicates.toArray(new Predicate[0]))).getResultList();
  }

  public T findFirst() {
    return em.createQuery(query.where(predicates.toArray(new Predicate[0]))).setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }
}
