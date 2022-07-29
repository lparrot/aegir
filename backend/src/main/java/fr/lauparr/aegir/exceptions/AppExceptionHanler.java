package fr.lauparr.aegir.exceptions;

import fr.lauparr.aegir.dto.api.ApiError;
import fr.lauparr.aegir.utils.ControllerUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
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
  public final ResponseEntity<?> handleApplicationException(ApplicationException e, WebRequest request) {
    ApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TaggedApplicationException.class)
  public final ResponseEntity<?> handleTaggedApplicationException(TaggedApplicationException e, WebRequest request) {
    ApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    exceptionResponse.getDetail().put("tag", e.getTag());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MessageException.class)
  public final ResponseEntity<?> handleMessageException(MessageException e, WebRequest request) {
    ApiError exceptionResponse = ControllerUtils.createMessageResponse(e.getColor(), e.getMessage(), e.getTitle());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<?> handlerConstraintViolationException(ConstraintViolationException e, WebRequest request) {
    return new ResponseEntity<>(ControllerUtils.createValidationResponse(ControllerUtils.createViolations(e)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<?> handleMessageException(Exception e, WebRequest request) throws Exception {
    ApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);

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

    return new ResponseEntity<>(exceptionResponse, headers, httpStatus);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(ControllerUtils.createValidationResponse(ControllerUtils.createViolations(e)), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiError exceptionResponse = ControllerUtils.createExceptionResponse(e);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
