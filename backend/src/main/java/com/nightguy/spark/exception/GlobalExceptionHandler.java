package com.nightguy.spark.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> handle(ResponseStatusException ex) {
    if (ex.getReason() != null) {
      return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
    }
    return ResponseEntity.status(ex.getStatusCode()).body(null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField, FieldError::getDefaultMessage, (first, second) -> first));

    return ResponseEntity.badRequest().body(errors);
  }
}
