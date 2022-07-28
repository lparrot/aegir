package fr.lauparr.aegir.exceptions;

import lombok.Getter;

public class TaggedApplicationException extends RuntimeException {

  @Getter
  private final String tag;

  public TaggedApplicationException(String tag) {
    super();
    this.tag = this.normalizeTag(tag);
  }

  public TaggedApplicationException(String tag, String message) {
    super(message);
    this.tag = this.normalizeTag(tag);
  }

  public TaggedApplicationException(String tag, String message, Throwable cause) {
    super(message, cause);
    this.tag = this.normalizeTag(tag);
  }

  public TaggedApplicationException(String tag, Throwable cause) {
    super(cause);
    this.tag = this.normalizeTag(tag);
  }

  protected TaggedApplicationException(String tag, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.tag = this.normalizeTag(tag);
  }

  private String normalizeTag(String tag) {
    if (tag == null) {
      return null;
    }
    return tag.toLowerCase();
  }
}
