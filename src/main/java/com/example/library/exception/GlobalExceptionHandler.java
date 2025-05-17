package com.example.library.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<String> details = ex.getBindingResult().getAllErrors().stream()
        .map(error -> {
          if (error instanceof FieldError) {
            return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
          }
          return error.getDefaultMessage();
        })
        .collect(Collectors.toList());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation Failed",
        "Erro de validação nos dados enviados",
        request.getRequestURI(),
        details);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DuplicateIsbnException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateIsbnException(DuplicateIsbnException ex,
      HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Duplicate ISBN",
        ex.getMessage(),
        request.getRequestURI(),
        List.of(ex.getMessage()));
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        "Ocorreu um erro inesperado",
        request.getRequestURI(),
        List.of(ex.getMessage()));
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}