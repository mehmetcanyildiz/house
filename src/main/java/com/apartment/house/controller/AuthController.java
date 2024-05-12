package com.apartment.house.controller;

import com.apartment.house.dto.auth.LoginRequestDTO;
import com.apartment.house.dto.auth.LoginResponseDTO;
import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.dto.auth.RegisterResponseDTO;
import com.apartment.house.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDTO) throws MessagingException {
        LoginResponseDTO response = authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerDTO) throws MessagingException {
        RegisterResponseDTO response = authService.register(registerDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public void confirmEmail(@RequestParam("token") String token) throws MessagingException {
        authService.activateAccount(token);
    }

}
