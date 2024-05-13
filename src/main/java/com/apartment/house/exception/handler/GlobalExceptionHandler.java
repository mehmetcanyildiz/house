package com.apartment.house.exception.handler;

import com.apartment.house.enums.BusinessErrorCodesEnum;
import com.apartment.house.service.LoggerService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  private final LoggerService loggerService;

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ExceptionResponse> handleException(LockedException e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        ExceptionResponse.builder().errorCode(BusinessErrorCodesEnum.ACCOUNT_LOCKED.getCode())
            .businessErrorDescription(BusinessErrorCodesEnum.ACCOUNT_LOCKED.getDescription())
            .error(e.getMessage()).build());
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ExceptionResponse> handleException(DisabledException e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        ExceptionResponse.builder().errorCode(BusinessErrorCodesEnum.ACCOUNT_DISABLED.getCode())
            .businessErrorDescription(BusinessErrorCodesEnum.ACCOUNT_DISABLED.getDescription())
            .error(e.getMessage()).build());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        ExceptionResponse.builder().errorCode(BusinessErrorCodesEnum.BAD_CREDENTIALS.getCode())
            .businessErrorDescription(BusinessErrorCodesEnum.BAD_CREDENTIALS.getDescription())
            .error(e.getMessage()).build());
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<ExceptionResponse> handleException(MessagingException e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ExceptionResponse.builder().error(e.getMessage()).build());
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ExceptionResponse> handleException(ExpiredJwtException e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ExceptionResponse.builder().error(e.getMessage()).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
    loggerService.logError(e.getMessage(), e);

    Set<String> validationErrors = new HashSet<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      var errorMessage = error.getDefaultMessage();
      validationErrors.add(errorMessage);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ExceptionResponse.builder().validationErrors(validationErrors).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleException(Exception e) {
    loggerService.logError(e.getMessage(), e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        ExceptionResponse.builder().businessErrorDescription("Internal error, contact the admin")
            .error(e.getMessage()).build());
  }
}
