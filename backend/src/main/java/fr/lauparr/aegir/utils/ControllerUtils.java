package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.dto.api.RestApiConstraint;
import fr.lauparr.aegir.dto.api.RestApiError;
import lombok.experimental.UtilityClass;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class ControllerUtils {

  public RestApiError createExceptionResponse(Throwable throwable) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("class", throwable.getClass());
    detail.put("trace", Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    return new RestApiError().setMessage(throwable.getMessage()).setType("exception").setDetail(detail);
  }

  public RestApiError createMessageResponse(String color, String message, String title) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("color", color);
    detail.put("title", title);
    return new RestApiError().setMessage(message).setType("message").setDetail(detail);
  }

  public RestApiError createMessageResponse(String color, String message) {
    return createMessageResponse(color, message, null);
  }

  public RestApiError createValidationResponse(List<RestApiConstraint> violations) {
    Map<String, Object> detail = new HashMap<>();
    violations.forEach(violation -> {
      detail.put(violation.getField(), violation);
    });
    return new RestApiError().setMessage(MessageUtils.getMessage("error.validation")).setType("validation").setDetail(detail);
  }

  public <T> void validate(T data) {
    Validator validator;
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
      Set<ConstraintViolation<T>> violations = validator.validate(data);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
    }
  }

  public List<RestApiConstraint> createViolations(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream().map(RestApiConstraint::new).collect(Collectors.toList());
  }

  public List<RestApiConstraint> createViolations(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getAllErrors().stream().map(RestApiConstraint::new).collect(Collectors.toList());
  }

  public List<RestApiConstraint> createViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream().map(RestApiConstraint::new).collect(Collectors.toList());
  }

}
