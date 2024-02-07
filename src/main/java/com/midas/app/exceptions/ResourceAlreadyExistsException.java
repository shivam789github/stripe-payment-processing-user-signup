package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends ApiException {

  static final String MESSAGE = "Resource already exists";

  public ResourceAlreadyExistsException() {
    super(HttpStatus.CONFLICT, MESSAGE);
  }

  public ResourceAlreadyExistsException(String message) {
    super(HttpStatus.CONFLICT, MESSAGE);

    super.setMessage(message);
  }

  public ResourceAlreadyExistsException(String message, List<String> errors) {
    super(HttpStatus.CONFLICT, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public ResourceAlreadyExistsException(String message, String error) {
    super(HttpStatus.CONFLICT, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
