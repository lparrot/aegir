package fr.lauparr.aegir.features.shared.db_request;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Service
public class DBRequestSrv {

  @PersistenceContext
  private EntityManager em;

  public <T> DBRequestBuilder<T> request(Class<T> clazz) {
    CriteriaQuery<T> query = query(clazz);
    return new DBRequestBuilder<>(em, builder(), query, query.from(clazz));
  }

  public <T> DBRequestBuilder<Tuple> tuple(Class<T> clazz) {
    CriteriaQuery<Tuple> query = query(Tuple.class);
    return new DBRequestBuilder<>(em, builder(), query, query.from(clazz));
  }

  private CriteriaBuilder builder() {
    return em.getCriteriaBuilder();
  }

  private <T> CriteriaQuery<T> query(Class<T> clazz) {
    return builder().createQuery(clazz);
  }


}
