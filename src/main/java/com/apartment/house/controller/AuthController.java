package com.apartment.house.controller;

import com.apartment.house.dto.auth.ActivateAccountRequestDTO;
import com.apartment.house.dto.auth.ActivateAccountResponseDTO;
import com.apartment.house.dto.auth.LoginRequestDTO;
import com.apartment.house.dto.auth.LoginResponseDTO;
import com.apartment.house.dto.auth.LogoutRequestDTO;
import com.apartment.house.dto.auth.LogoutResponseDTO;
import com.apartment.house.dto.auth.PasswordResetClientRequestDTO;
import com.apartment.house.dto.auth.PasswordResetClientResponseDTO;
import com.apartment.house.dto.auth.PasswordResetRequestDTO;
import com.apartment.house.dto.auth.PasswordResetResponseDTO;
import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.dto.auth.RegisterResponseDTO;
import com.apartment.house.service.AuthService;
import com.apartment.house.service.LoggerService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API Endpoints for Authentication and Authorization")
public class AuthController {

  private final AuthService authService;
  private final LoggerService loggerService;

  @Operation(summary = "Login", description = "Login to the system", responses = {
      @ApiResponse(responseCode = "200", description = "Login successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDTO)
      throws ExpiredJwtException {
    LoginResponseDTO response = authService.login(loginDTO);
    loggerService.logInfo(loginDTO.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Register", description = "Register to the system", responses = {
      @ApiResponse(responseCode = "200", description = "Registration successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerDTO)
      throws Exception {
    RegisterResponseDTO response = authService.register(registerDTO);
    loggerService.logInfo(registerDTO.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Activate Account", description = "Activate account", responses = {
      @ApiResponse(responseCode = "200", description = "Account activated"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @GetMapping("/activate-account")
  public ResponseEntity<?> activateAccount(@Valid ActivateAccountRequestDTO activateDTO)
      throws MessagingException {
    ActivateAccountResponseDTO response = authService.activateAccount(activateDTO);
    loggerService.logInfo(response.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }


  @Operation(summary = "Password Reset Client", description = "Password reset for client", responses = {
      @ApiResponse(responseCode = "200", description = "Password reset successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @PostMapping("/password-reset-client")
  public ResponseEntity<?> passwordResetClient(
      @RequestBody @Valid PasswordResetClientRequestDTO resetRequestDTO) throws Exception {
    PasswordResetClientResponseDTO response = authService.passwordResetClient(resetRequestDTO);
    loggerService.logInfo(response.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Password Reset", description = "Password reset", responses = {
      @ApiResponse(responseCode = "200", description = "Password reset successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @PutMapping("/password-reset")
  public ResponseEntity<?> passwordReset(@RequestBody @Valid PasswordResetRequestDTO resetDTO)
      throws MessagingException {
    PasswordResetResponseDTO response = authService.passwordReset(resetDTO);
    loggerService.logInfo(response.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Logout", description = "Logout from the system", responses = {
      @ApiResponse(responseCode = "200", description = "Logout successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),})
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody @Valid LogoutRequestDTO logoutDTO) {
    LogoutResponseDTO response = authService.logout(logoutDTO);
    loggerService.logInfo(response.getEmail() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

}
