package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.config.AutowireHelper;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

@UtilityClass
public class MessageUtils {

  public String getMessage(final String code, final Object... properties) {
    if (AutowireHelper.getApplicationContext() == null) {
      return "";
    }

    final MessageSource messageSource = AutowireHelper.getBean(MessageSource.class);

    return messageSource.getMessage(code, properties, Locale.getDefault());
  }

}
