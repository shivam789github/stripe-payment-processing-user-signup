package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

  static final String MESSAGE = "Unauthorized";

  public UnauthorizedException() {
    super(HttpStatus.UNAUTHORIZED, MESSAGE);
  }

  public UnauthorizedException(String message) {
    super(HttpStatus.UNAUTHORIZED, MESSAGE);

    super.setMessage(message);
  }

  public UnauthorizedException(String message, List<String> errors) {
    super(HttpStatus.UNAUTHORIZED, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public UnauthorizedException(String message, String error) {
    super(HttpStatus.UNAUTHORIZED, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
