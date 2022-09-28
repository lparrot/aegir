package fr.lauparr.aegir.features.shared.db_request;

import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static fr.lauparr.aegir.utils.DaoUtils.convertToDto;

public class DBRequestBuilder<T> extends AbstractDBRequest<T, DBRequestBuilder<T>> {

  private final CriteriaQuery<T> query;

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

  public DBRequestBuilder<T> where(UnaryOperator<DBRequestBuilder<T>> function) {
    predicates = andCondition(predicates, andCondition(function.apply(new DBRequestBuilder<>(em, builder, query, root, paths)).getPredicates()));
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

  public DBRequestBuilder<T> whereExists(Class<?> rootClass, BiFunction<DBSubrequestBuilder<T>, Root<?>, DBSubrequestBuilder<T>> function) {
    DBSubrequestBuilder<T> subrequestBuilder = function.apply(new DBSubrequestBuilder<>(em, builder, query, rootClass), root);
    predicates = andCondition(predicates, builder.exists(subrequestBuilder.build()));
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    query.multiselect(Arrays.stream(fields).map(field -> path(field).alias(field)).toArray(Selection[]::new));
    return this;
  }

  public DBRequestBuilder<T> orderBy(String field, String order) {
    ArrayList<Order> orders = new ArrayList<>(query.getOrderList());
    switch (order) {
      case "desc":
        orders.add(builder.desc(path(field)));
        break;
      case "asc":
      default:
        orders.add(builder.asc(path(field)));
        break;
    }

    query.orderBy(orders);

    return this;
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

  private TypedQuery<T> createQuery() {
    TypedQuery<T> jpaQuery;
    if (predicates == null) {
      jpaQuery = em.createQuery(query);
    } else {
      jpaQuery = em.createQuery(query.where(predicates));
    }
    return jpaQuery;
  }
}
