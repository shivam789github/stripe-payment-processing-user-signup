package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

  static final String MESSAGE = "Resource not found";

  public ResourceNotFoundException() {
    super(HttpStatus.NOT_FOUND, MESSAGE);
  }

  public ResourceNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, MESSAGE);

    super.setMessage(message);
  }

  public ResourceNotFoundException(String message, List<String> errors) {
    super(HttpStatus.NOT_FOUND, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public ResourceNotFoundException(String message, String error) {
    super(HttpStatus.NOT_FOUND, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
