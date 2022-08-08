package fr.lauparr.aegir.utils;

import fr.lauparr.aegir.dto.api.ApiConstraint;
import fr.lauparr.aegir.dto.api.ApiError;
import lombok.experimental.UtilityClass;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class ControllerUtils {

  public ApiError createExceptionResponse(Throwable throwable) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("class", throwable.getClass());
    detail.put("trace", Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    return new ApiError().setMessage(throwable.getMessage()).setType("exception").setDetail(detail);
  }

  public ApiError createMessageResponse(String color, String message, String title) {
    Map<String, Object> detail = new HashMap<>();
    detail.put("color", color);
    detail.put("title", title);
    return new ApiError().setMessage(message).setType("message").setDetail(detail);
  }

  public ApiError createMessageResponse(String color, String message) {
    return createMessageResponse(color, message, null);
  }

  public ApiError createValidationResponse(List<ApiConstraint> violations) {
    Map<String, Object> detail = new HashMap<>();
    violations.forEach(violation -> {
      detail.put(violation.getField(), violation);
    });
    return new ApiError().setMessage(MessageUtils.getMessage("error.validation")).setType("validation").setDetail(detail);
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

  public List<ApiConstraint> createViolations(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

  public List<ApiConstraint> createViolations(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getAllErrors().stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

  public List<ApiConstraint> createViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream().map(ApiConstraint::new).collect(Collectors.toList());
  }

}
