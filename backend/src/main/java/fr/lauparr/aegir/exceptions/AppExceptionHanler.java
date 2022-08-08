package fr.lauparr.aegir.exceptions;

import fr.lauparr.aegir.dto.api.RestApiError;
import fr.lauparr.aegir.utils.ControllerUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class AppExceptionHanler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final ResponseEntity<RestApiError> handleApplicationException(ApplicationException e, WebRequest request) {
    RestApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TaggedApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final ResponseEntity<RestApiError> handleTaggedApplicationException(TaggedApplicationException e, WebRequest request) {
    RestApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    exceptionResponse.getDetail().put("tag", e.getTag());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MessageException.class)
  @ResponseStatus(HttpStatus.OK)
  public final ResponseEntity<RestApiError> handleMessageException(MessageException e, WebRequest request) {
    RestApiError exceptionResponse = ControllerUtils.createMessageResponse(e.getColor(), e.getMessage(), e.getTitle());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<RestApiError> handlerConstraintViolationException(ConstraintViolationException e, WebRequest request) {
    return new ResponseEntity<>(ControllerUtils.createValidationResponse(ControllerUtils.createViolations(e)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<RestApiError> handlerAccessDeniedException(AccessDeniedException e, WebRequest request) {
    return new ResponseEntity<>(ControllerUtils.createExceptionResponse(e), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final ResponseEntity<RestApiError> handleAllException(Exception e, WebRequest request) {
    RestApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);

    if (e instanceof ResponseStatusException) {
      return new ResponseEntity<>(exceptionResponse, ((ResponseStatusException) e).getResponseHeaders(), ((ResponseStatusException) e).getStatus());
    }

    final ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);

    if (responseStatus != null) {
      return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), responseStatus.value());
    }

    ResponseEntity<Object> responseEntity;
    try {
      responseEntity = super.handleException(e, request);
    } catch (Exception exception) {
      responseEntity = null;
    }

    HttpHeaders headers = responseEntity == null ? new HttpHeaders() : responseEntity.getHeaders();
    HttpStatus httpStatus = responseEntity == null ? HttpStatus.INTERNAL_SERVER_ERROR : responseEntity.getStatusCode();

    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    return new ResponseEntity<>(exceptionResponse, headers, httpStatus);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(ControllerUtils.createValidationResponse(ControllerUtils.createViolations(e)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
    RestApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
