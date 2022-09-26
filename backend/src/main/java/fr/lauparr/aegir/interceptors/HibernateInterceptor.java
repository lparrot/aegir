package fr.lauparr.aegir.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

@Service
@Slf4j
public class HibernateInterceptor extends EmptyInterceptor {

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
    log.debug(MessageFormat.format("Entity {0}#{1} changed from {2} to {3}",
      entity.getClass().getSimpleName(),
      id,
      Arrays.toString(previousState),
      Arrays.toString(currentState))
    );
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }
}
