package com.example.library.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private List<String> details;

  public ErrorResponse(int status, String error, String message, String path, List<String> details) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.details = details;
  }

}