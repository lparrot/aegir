package fr.lauparr.aegir.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class MessageException extends RuntimeException {
  @Getter
  private final String color;
  @Getter
  private final String title;

  @Builder
  public MessageException(String color, String message, String title, Throwable cause) {
    super(message, cause);
    this.color = StringUtils.isBlank(color) ? "negative" : color;
    this.title = title;
  }
}
