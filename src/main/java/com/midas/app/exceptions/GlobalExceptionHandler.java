package com.midas.app.exceptions;

import com.midas.generated.model.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger lgr = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    List<String> errors = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + " -> " + error.getDefaultMessage());
    }

    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + " -> " + error.getDefaultMessage());
    }

    String msg = String.join(", ", errors);

    ErrorDto err = getErrorDto(HttpStatus.BAD_REQUEST, "Invalid method arguments", msg);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    String msg = ex.getParameterName() + " parameter is missing";

    ErrorDto err = getErrorDto(HttpStatus.BAD_REQUEST, "Invalid method arguments", msg);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String msg = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    ErrorDto err = getErrorDto(HttpStatus.NOT_FOUND, "No handler found", msg);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");

    // sonarlint raising bug here, need to fix it
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" ")); // NOSONAR

    ErrorDto err =
        getErrorDto(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", builder.toString());

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

    ErrorDto err =
        getErrorDto(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Media type not supported", builder.toString());

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(err);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    errors.add(ex.getLocalizedMessage());

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(
          violation.getRootBeanClass().getName()
              + " "
              + violation.getPropertyPath()
              + ": "
              + violation.getMessage());
    }

    String msg = String.join(", ", errors);

    ErrorDto err = getErrorDto(HttpStatus.BAD_REQUEST, "Constraints violation", msg);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {

    StringBuilder builder = new StringBuilder();
    builder.append(ex.getName()).append(" should be of type ");

    if (ex.getRequiredType() != null) {
      builder.append(Objects.requireNonNull(ex.getRequiredType()).getName());
    }

    ErrorDto err =
        getErrorDto(HttpStatus.BAD_REQUEST, "Method argument type mismatch", builder.toString());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
    lgr.error("Unexpected error occurred", ex);

    ErrorDto err =
        getErrorDto(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error",
            "Something went wrong",
            "Internal Error");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
  }

  // Custom defined exceptions

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(ApiException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());

    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());

    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());

    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<Object> handleAlreadyExistsException(ResourceAlreadyExistsException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());

    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());
    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
    ErrorDto err =
        getErrorDto(
            ex.getApiError().getCode(), ex.getApiError().getTitle(), ex.getApiError().getMessage());

    return ResponseEntity.status(ex.getApiError().getCode()).body(err);
  }

  private ErrorDto getErrorDto(HttpStatus code, String title, String msg) {
    ErrorDto err = new ErrorDto();

    err.setType("");

    return err.code(code.toString()).message(msg).title(title);
  }

  private ErrorDto getErrorDto(HttpStatus code, String title, String msg, String type) {
    ErrorDto err = new ErrorDto();

    return err.code(code.toString()).message(msg).title(title).type(type);
  }
}
