package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.dto.api.ApiConstraint;
import fr.lauparr.aegir.dto.api.ApiError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ControllerUtils {

  public static ApiError createExceptionResponse(Throwable throwable) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("class", throwable.getClass());
    detail.put("trace", Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    return new ApiError().setMessage(throwable.getMessage()).setType("exception").setDetail(detail);
  }

  public static ApiError createMessageResponse(String color, String message, String title) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("color", color);
    detail.put("title", title);
    return new ApiError().setMessage(message).setType("message").setDetail(detail);
  }

  public static ApiError createMessageResponse(String color, String message) {
    return createMessageResponse(color, message, null);
  }

  public static ApiError createValidationResponse(List<ApiConstraint> violations) {
    Map<String, Object> detail = new HashMap<>();
    violations.forEach(violation -> {
      detail.put(violation.getField(), violation);
    });
    return new ApiError().setMessage(MessageUtils.getMessage("error.validation")).setType("validation").setDetail(detail);
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
