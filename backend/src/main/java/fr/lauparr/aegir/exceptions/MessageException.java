package fr.lauparr.aegir.exceptions;

import fr.lauparr.aegir.utils.MessageUtils;
import lombok.Getter;

import java.util.logging.Level;

public class MessageException extends RuntimeException {
  @Getter
  private final Level level;
  @Getter
  private final String title;

  public MessageException(Level level, String message) {
    super(message);
    this.level = level;
    this.title = null;
  }

  public MessageException(Level level, String message, Throwable cause) {
    super(message, cause);
    this.level = level;
    this.title = null;
  }

  public MessageException(String message) {
    this(Level.SEVERE, message);
  }

  public MessageException(String message, Throwable cause) {
    this(Level.SEVERE, message, cause);
  }

  public MessageException(Level level, String message, String title) {
    super(message);
    this.level = level;
    this.title = title;
  }

  public MessageException(Level level, String message, String title, Throwable cause) {
    super(message, cause);
    this.level = level;
    this.title = title;
  }

  public MessageException(String message, String title) {
    this(Level.SEVERE, message, title);
  }

  public MessageException(String message, String title, Throwable cause) {
    this(Level.SEVERE, message, title, cause);
  }

  public static MessageException withMessage(String message, Object... properties) {
    return new MessageException(MessageUtils.getMessage(message, properties));
  }
}
