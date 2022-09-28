package fr.lauparr.aegir.features.shared.db_request;

import fr.lauparr.aegir.utils.DaoUtils;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
    this.predicates = this.andCondition(this.predicates, this.andCondition(function.apply(new DBRequestBuilder<>(this.em, this.builder, this.query, this.root, this.paths)).getPredicates()));
    return this;
  }

  public DBRequestBuilder<T> orWhere(UnaryOperator<DBRequestBuilder<T>> function) {
    this.predicates = this.orCondition(this.predicates, this.andCondition(function.apply(new DBRequestBuilder<>(this.em, this.builder, this.query, this.root, this.paths)).getPredicates()));
    return this;
  }

  public DBRequestBuilder<T> whereNot(UnaryOperator<DBRequestBuilder<T>> function) {
    this.predicates = this.andCondition(this.predicates, this.builder.not(function.apply(new DBRequestBuilder<>(this.em, this.builder, this.query, this.root, this.paths)).getPredicates()));
    return this;
  }

  public DBRequestBuilder<T> whereExists(Class<?> rootClass, BiFunction<DBSubrequestBuilder<T>, Root<?>, DBSubrequestBuilder<T>> function) {
    DBSubrequestBuilder<T> subrequestBuilder = function.apply(new DBSubrequestBuilder<>(this.em, this.builder, this.query, rootClass), this.root);
    this.predicates = this.andCondition(this.predicates, this.builder.exists(subrequestBuilder.build()));
    return this;
  }

  public DBRequestBuilder<T> select(String... fields) {
    this.query.multiselect(Arrays.stream(fields).map(field -> this.path(field).alias(field)).toArray(Selection[]::new));
    return this;
  }

  public DBRequestBuilder<T> orderBy(String field, String order) {
    ArrayList<Order> orders = new ArrayList<>(this.query.getOrderList());
    switch (order) {
      case "desc":
        orders.add(this.builder.desc(this.path(field)));
        break;
      case "asc":
      default:
        orders.add(this.builder.asc(this.path(field)));
        break;
    }

    this.query.orderBy(orders);

    return this;
  }

  public String sql() {
    return this.createQuery().unwrap(Query.class).getQueryString();
  }

  public List<T> list() {
    return this.createQuery().getResultList();
  }

  public T first() {
    return this.createQuery().setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }

  public <U> List<U> projection(Class<U> projectionClass) {
    return this.list().stream().map(data -> DaoUtils.convertToDto(data, projectionClass)).collect(Collectors.toList());
  }

  private TypedQuery<T> createQuery() {
    TypedQuery<T> jpaQuery;
    if (this.predicates == null) {
      jpaQuery = this.em.createQuery(this.query);
    } else {
      jpaQuery = this.em.createQuery(this.query.where(this.predicates));
    }
    return jpaQuery;
  }
}
