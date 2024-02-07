package com.midas.app.exceptions;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

  private final transient ApiError apiError;

  public ApiException(HttpStatus code, String title) {
    super(title);
    apiError = new ApiError(code, title);
  }

  public void setMessage(String message) {
    this.apiError.setMessage(message);
  }

  public void setErrors(List<String> errors) {
    this.apiError.setErrors(errors);
  }

  public void setErrors(String error) {
    this.apiError.setErrors(Collections.singletonList(error));
  }
}
