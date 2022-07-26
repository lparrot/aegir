package fr.lauparr.aegir.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AutowireHelper implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  private AutowireHelper() {
  }

  public static void autowire(Object classToAutowire) {
    AutowireHelper.applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
  }

  public static <T> T getBean(Class<T> classToAutowire) {
    return AutowireHelper.applicationContext.getBean(classToAutowire);
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) {
    AutowireHelper.applicationContext = applicationContext;
  }
}
