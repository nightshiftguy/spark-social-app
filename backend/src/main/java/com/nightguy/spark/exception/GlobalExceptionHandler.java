package com.nightguy.spark.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
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
                    // replacing null with "" for toMap function
                    FieldError::getField,
                    fieldError -> {
                      var message = fieldError.getDefaultMessage();
                      return message != null ? message : "";
                    },
                    (first, _) -> first));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(ConversionFailedException.class)
  public ResponseEntity<String> handleConflict(RuntimeException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
