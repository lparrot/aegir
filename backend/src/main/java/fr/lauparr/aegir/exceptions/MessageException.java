package fr.lauparr.aegir.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MessageException extends RuntimeException {
  private String color;
  private String title;

  public MessageException() {
  }

  public MessageException(String message) {
    super(message);
  }

  public MessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageException(Throwable cause) {
    super(cause);
  }

  public MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
