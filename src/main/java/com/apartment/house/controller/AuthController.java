package com.apartment.house.controller;

import com.apartment.house.dto.auth.ActivateAccountRequestDTO;
import com.apartment.house.dto.auth.ActivateAccountResponseDTO;
import com.apartment.house.dto.auth.LoginRequestDTO;
import com.apartment.house.dto.auth.LoginResponseDTO;
import com.apartment.house.dto.auth.PasswordResetClientRequestDTO;
import com.apartment.house.dto.auth.PasswordResetClientResponseDTO;
import com.apartment.house.dto.auth.PasswordResetRequestDTO;
import com.apartment.house.dto.auth.PasswordResetResponseDTO;
import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.dto.auth.RegisterResponseDTO;
import com.apartment.house.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
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
public class AuthController {

  private final AuthService authService;


  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDTO)
      throws ExpiredJwtException {
    LoginResponseDTO response = authService.login(loginDTO);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerDTO)
      throws Exception {
    RegisterResponseDTO response = authService.register(registerDTO);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/activate-account")
  public ResponseEntity<?> activateAccount(@Valid ActivateAccountRequestDTO activateDTO)
      throws MessagingException {
    ActivateAccountResponseDTO response = authService.activateAccount(activateDTO);
    return ResponseEntity.ok(response);
  }


  @PostMapping("/password-reset-client")
  public ResponseEntity<?> passwordResetClient(
      @RequestBody @Valid PasswordResetClientRequestDTO resetRequestDTO)
      throws Exception {
    PasswordResetClientResponseDTO response = authService.passwordResetClient(resetRequestDTO);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/password-reset")
  public ResponseEntity<?> passwordReset(@RequestBody @Valid PasswordResetRequestDTO resetDTO)
      throws MessagingException {
    PasswordResetResponseDTO response = authService.passwordReset(resetDTO);
    return ResponseEntity.ok(response);
  }

}
