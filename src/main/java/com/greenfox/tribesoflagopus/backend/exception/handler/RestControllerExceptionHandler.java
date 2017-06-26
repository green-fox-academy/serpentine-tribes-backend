package com.greenfox.tribesoflagopus.backend.exception.handler;

import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    StatusResponse messageNotReadableError = StatusResponse.builder()
        .status("error")
        .message("Missing input")
        .build();

    return super.handleExceptionInternal(ex, messageNotReadableError, headers, status, request);
  }
}
