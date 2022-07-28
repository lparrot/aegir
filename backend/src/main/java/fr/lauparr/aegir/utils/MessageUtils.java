package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.config.AutowireHelper;
import org.springframework.context.MessageSource;

import java.util.Locale;

public interface MessageUtils {

  static String getMessage(final String code, final Object... properties) {
    if (AutowireHelper.getApplicationContext() == null) {
      return "";
    }

    final MessageSource messageSource = AutowireHelper.getBean(MessageSource.class);

    return messageSource.getMessage(code, properties, Locale.getDefault());
  }

}
