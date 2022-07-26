package fr.lauparr.aegir.config;

import fr.lauparr.aegir.annotations.RestDisabled;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class RequestMappingConfig extends RequestMappingHandlerMapping {

  @Autowired
  private BeanFactory beanFactory;

  @Override
  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    RestDisabled annotation = AnnotationUtils.findAnnotation(method, RestDisabled.class);

    if (annotation != null) {
      if (StringUtils.isNotBlank(annotation.condition())) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(this.beanFactory));
        Object bean = this.beanFactory.getBean(handlerType);
        context.setVariable("current", bean);
        Boolean condition = parser.parseExpression(annotation.condition()).getValue(context, Boolean.class);
        if (condition != null && condition) {
          return null;
        }
      } else {
        return null;
      }
    }
    return super.getMappingForMethod(method, handlerType);
  }
}
