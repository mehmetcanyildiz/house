package com.apartment.house.service;

import com.apartment.house.model.dto.auth.LoginRequestDTO;
import com.apartment.house.model.dto.auth.LoginResponseDTO;
import com.apartment.house.model.dto.auth.RegisterRequestDTO;
import com.apartment.house.model.dto.auth.RegisterResponseDTO;
import jakarta.mail.MessagingException;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    RegisterResponseDTO register(RegisterRequestDTO registerDTO);

    void activateAccount(String token) throws MessagingException;
}