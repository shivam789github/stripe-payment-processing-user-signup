package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ApiException {

  static final String MESSAGE = "Invalid request";

  public InvalidRequestException() {
    super(HttpStatus.BAD_REQUEST, MESSAGE);
  }

  public InvalidRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, MESSAGE);

    super.setMessage(message);
  }

  public InvalidRequestException(String message, List<String> errors) {
    super(HttpStatus.BAD_REQUEST, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public InvalidRequestException(String message, String error) {
    super(HttpStatus.BAD_REQUEST, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
