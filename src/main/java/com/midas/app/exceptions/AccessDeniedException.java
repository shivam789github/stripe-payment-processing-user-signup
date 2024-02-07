package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException {

  static final String MESSAGE = "Access Denied";

  public AccessDeniedException() {
    super(HttpStatus.FORBIDDEN, MESSAGE);
  }

  public AccessDeniedException(String message) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
  }

  public AccessDeniedException(String message, List<String> errors) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public AccessDeniedException(String message, String error) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
