package fr.lauparr.aegir.features.shared.db_request;

import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Subquery;
import java.util.HashMap;
import java.util.Map;

public class DBSubrequestBuilder<T> extends AbstractDBRequest<T, DBSubrequestBuilder<T>> {

  @Getter
  private Subquery subquery;

  public DBSubrequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery query, Class rootClass) {
    this(em, builder, query, rootClass, null);
  }

  public DBSubrequestBuilder(EntityManager em, CriteriaBuilder builder, CriteriaQuery query, Class rootClass, Map<String, Path> paths) {
    this.em = em;
    this.builder = builder;
    this.subquery = query.subquery(rootClass);
    this.root = subquery.from(rootClass);
    this.paths = paths == null ? new HashMap<>() : paths;
  }

  public DBSubrequestBuilder<T> select(String field) {
    subquery.select(path(field));
    return this;
  }

  public Subquery build() {
    if (predicates == null) {
      return subquery;
    }
    return subquery.where(predicates);
  }

}
