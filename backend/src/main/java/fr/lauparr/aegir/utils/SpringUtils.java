package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.config.AutowireHelper;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class SpringUtils {

  public String getProperty(final String key) {
    return AutowireHelper.getBean(Environment.class).getProperty(key);
  }

  public <T> T getProperty(final String key, final Class<T> clazz) {
    return AutowireHelper.getBean(Environment.class).getProperty(key, clazz);
  }

  public <T extends UserDetails> T getCurrentUser() {
    return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public String getUrlFromRequest(final HttpServletRequest request) {
    try {
      if (request != null) {
        return ServletUriComponentsBuilder.fromServletMapping(request).build().toString();
      }
    } catch (final IllegalStateException e) {
      return "";
    }
    return null;
  }

  public HttpServletRequest getRequest() {
    final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
    return null;
  }

  public <T> void validate(final T data) {
    final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    final Validator validator = factory.getValidator();
    final Set<ConstraintViolation<T>> violations = validator.validate(data);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  public Object getExpressionValue(final String expression, final Map<String, Object> contextVariables) {
    final BeanFactory beanFactory = AutowireHelper.getApplicationContext().getAutowireCapableBeanFactory();

    final ExpressionParser parser = new SpelExpressionParser();
    final StandardEvaluationContext context = new StandardEvaluationContext();
    context.setBeanResolver(new BeanFactoryResolver(beanFactory));
    contextVariables.forEach(context::setVariable);
    return parser.parseExpression(expression).getValue(context);
  }

}
