package com.apartment.house.controller;

import com.apartment.house.model.dto.auth.LoginRequestDTO;
import com.apartment.house.model.dto.auth.LoginResponseDTO;
import com.apartment.house.model.dto.auth.RegisterRequestDTO;
import com.apartment.house.model.dto.auth.RegisterResponseDTO;
import com.apartment.house.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        LoginResponseDTO response = authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerDTO) {
        RegisterResponseDTO response = authService.register(registerDTO);

        return ResponseEntity.ok(response);
    }

}
