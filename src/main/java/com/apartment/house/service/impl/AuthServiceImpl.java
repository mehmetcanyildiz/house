package com.apartment.house.service.impl;

import com.apartment.house.exception.auth.LoginException;
import com.apartment.house.exception.auth.RegisterException;
import com.apartment.house.model.dto.auth.LoginRequestDTO;
import com.apartment.house.model.dto.auth.LoginResponseDTO;
import com.apartment.house.model.dto.auth.RegisterRequestDTO;
import com.apartment.house.model.dto.auth.RegisterResponseDTO;
import com.apartment.house.model.entity.User;
import com.apartment.house.repository.UserRepository;
import com.apartment.house.service.AuthService;
import com.apartment.house.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.SneakyThrows;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @SneakyThrows
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = new LoginResponseDTO();
        try {
            if (loginRequestDTO.getEmail().isEmpty() || loginRequestDTO.getPassword().isEmpty()) {
                throw new LoginException("Email and password are required");
            }

            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(loginRequestDTO.getEmail()));
            if (user.isEmpty()) {
                throw new LoginException("User not found");
            }

            if (!user.get().getPassword().equals(encryptPassword(loginRequestDTO.getPassword()))) {
                throw new LoginException("Invalid password");
            }


            response.setStatus(true);
            response.setToken(jwtTokenUtil.generateToken(user.get().getEmail()));
            response.setMessage("Login successful");


            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());

            return response;
        }
    }

    @SneakyThrows
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerDTO) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        try {
            validateRegister(registerDTO);
            User user = covertUserModel(registerDTO);
            userRepository.save(user);

            response.setStatus(true);
            response.setMessage("User registered successfully");

            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());

            return response;
        }
    }

    private User covertUserModel(RegisterRequestDTO registerDTO) {
        User user = new User();

        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstname());
        user.setLastName(registerDTO.getLastname());
        user.setPassword(encryptPassword(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());

        return user;
    }

    private void validateRegister(RegisterRequestDTO registerDTO) throws RegisterException {
        if (registerDTO.getEmail().isEmpty() || registerDTO.getPassword().isEmpty()) {
            throw new RegisterException("Email and password are required");
        }

        if (registerDTO.getFirstname().isEmpty() || registerDTO.getLastname().isEmpty()) {
            throw new RegisterException("Firstname and lastname are required");
        }

        if (registerDTO.getPassword().length() < 6) {
            throw new RegisterException("Password must be at least 6 characters long");
        }

        if (!registerDTO.getPassword().equals(registerDTO.getRe_password())) {
            throw new RegisterException("Passwords do not match");
        }

        if (registerDTO.getPhone().length() < 10) {
            throw new RegisterException("Phone number must be at least 10 characters long");
        }

        if (!registerDTO.getEmail().contains("@")) {
            throw new RegisterException("Invalid email");
        }

        if (!registerDTO.getEmail().contains(".")) {
            throw new RegisterException("Invalid email");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RegisterException("User already exists");
        }

        if (userRepository.existsByPhone(registerDTO.getPhone())) {
            throw new RegisterException("Phone number already exists");
        }
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(password);
    }
}
