package com.apartment.house.exception.handler;

import com.apartment.house.enums.BusinessErrorCodesEnum;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ExceptionResponse> handleException(LockedException e) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ExceptionResponse.builder()
                  .businessErrorCode(BusinessErrorCodesEnum.ACCOUNT_LOCKED.getCode())
                  .businessErrorDescription(BusinessErrorCodesEnum.ACCOUNT_LOCKED.getDescription())
                  .error(e.getMessage())
                  .build());
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ExceptionResponse> handleException(DisabledException e) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ExceptionResponse.builder()
                  .businessErrorCode(BusinessErrorCodesEnum.ACCOUNT_DISABLED.getCode())
                  .businessErrorDescription(
                      BusinessErrorCodesEnum.ACCOUNT_DISABLED.getDescription())
                  .error(e.getMessage())
                  .build());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException e) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ExceptionResponse.builder()
                  .businessErrorCode(BusinessErrorCodesEnum.BAD_CREDENTIALS.getCode())
                  .businessErrorDescription(BusinessErrorCodesEnum.BAD_CREDENTIALS.getDescription())
                  .error(e.getMessage())
                  .build());
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<ExceptionResponse> handleException(MessagingException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ExceptionResponse.builder()
                .error(e.getMessage())
                .build()
        );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
    Set<String> validationErrors = new HashSet<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      var errorMessage = error.getDefaultMessage();
      validationErrors.add(errorMessage);
    });

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
            ExceptionResponse.builder()
                .validationErrors(validationErrors)
                .build()
        );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleException(Exception e) {
    // Log the exception
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ExceptionResponse.builder()
                  .businessErrorDescription("Internal error, contact the admin")
                  .error(e.getMessage())
                  .build()
        );
  }
}
