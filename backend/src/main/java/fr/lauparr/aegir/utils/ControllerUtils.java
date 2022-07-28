package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.dto.ApiConstraint;
import fr.lauparr.aegir.dto.ResponseError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class ControllerUtils {

  public static ResponseError createExceptionResponse(Throwable throwable) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("class", throwable.getClass());
    detail.put("trace", Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    return ResponseError.builder().error(throwable.getMessage()).type("exception").detail(detail).build();
  }

  public static ResponseError createMessageResponse(Level level, String message, String title) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("level", level.getName());
    detail.put("title", title);
    return ResponseError.builder().error(message).type("message").detail(detail).build();
  }

  public static ResponseError createMessageResponse(Level level, String message) {
    return createMessageResponse(level, message, null);
  }

  public static ResponseError createValidationResponse(List<ApiConstraint> violations) {
    Map<String, Object> detail = new HashMap<>();
    violations.forEach(violation -> {
      detail.put(violation.getField(), violation);
    });
    return ResponseError.builder().error(MessageUtils.getMessage("error.validation")).type("validation").detail(detail).build();
  }

  public static <T> void validate(T data) {
    Validator validator;
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
      Set<ConstraintViolation<T>> violations = validator.validate(data);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
    }
  }

  public static List<ApiConstraint> createViolations(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

  public static List<ApiConstraint> createViolations(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getAllErrors().stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

  public static List<ApiConstraint> createViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

}
