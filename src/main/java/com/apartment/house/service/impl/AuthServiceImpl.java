package com.apartment.house.service.impl;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.exception.auth.LoginException;
import com.apartment.house.exception.auth.RegisterException;
import com.apartment.house.model.dto.auth.LoginRequestDTO;
import com.apartment.house.model.dto.auth.LoginResponseDTO;
import com.apartment.house.model.dto.auth.RegisterRequestDTO;
import com.apartment.house.model.dto.auth.RegisterResponseDTO;
import com.apartment.house.model.entity.Token;
import com.apartment.house.model.entity.User;
import com.apartment.house.model.enums.EmailTemplateNameEnum;
import com.apartment.house.model.enums.StatusEnum;
import com.apartment.house.repository.TokenRepository;
import com.apartment.house.repository.UserRepository;
import com.apartment.house.security.JwtService;
import com.apartment.house.service.AuthService;
import com.apartment.house.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.SneakyThrows;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final ApplicationConfig applicationConfig;
    private final AuthenticationManager autenticationManager;
    private final JwtService jwtService;

    @SneakyThrows
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = new LoginResponseDTO();
        try {
            validateUser(loginRequestDTO);
            var auth = autenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );
            var claims = new HashMap<String, Object>();
            var user = ((User)auth.getPrincipal());
            if (user.getStatus() != StatusEnum.ACTIVE) {
                Token token = tokenRepository.findByUser(user).stream().findFirst().orElseThrow();
                if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
                    sendValidationEmail(token.getUser());
                    throw new RuntimeException("Activation Token has expired. A new token has been sent to your email");
                }
                throw new LoginException("Account not activated. Please check your email for activation link");
            }
            claims.put("firstName", user.getFirstName());
            claims.put("lastname", user.getLastName());
            claims.put("email", user.getEmail());

            var jwtToken = jwtService.generateToken(claims,user);
            response.setStatus(true);
            response.setToken(jwtToken);
            response.setMessage("User logged in successfully");

            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());

            return response;
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO registerDTO) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        try {
            validateRegister(registerDTO);
            User user = covertUserModel(registerDTO);
            userRepository.save(user);
            sendValidationEmail(user);
            response.setStatus(true);
            response.setMessage("User registered successfully");

            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());

            return response;
        }
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation Token has expired. A new token has been sent to your email");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setStatus(StatusEnum.ACTIVE);
        userRepository.save(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        var activationUrl = applicationConfig.baseUrl + "/auth/activate?token=" + newToken;

        emailService.sendEmail(
                user.getEmail(),
                user.getFirstName(),
                EmailTemplateNameEnum.ACTIVATION_EMAIL,
                activationUrl,
                (String) newToken,
                "Account activation"
        );
    }

    private Object generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken(6);
        Token token = createTokenModel(user, generatedToken);
        tokenRepository.save(token);

        return generatedToken;
    }

    private User covertUserModel(RegisterRequestDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstname());
        user.setLastName(registerDTO.getLastname());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());

        return user;
    }

    private String generateActivationToken(int length) {
        String numbers = "0123456789";
        StringBuilder token = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(numbers.length());
            token.append(numbers.charAt(index));
        }

        return token.toString();
    }

    private Token createTokenModel(User user, String token) {
        Token tokenModel = new Token();
        tokenModel.setToken(token);
        tokenModel.setUser(user);
        tokenModel.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        return tokenModel;
    }

    private void validateRegister(RegisterRequestDTO registerDTO) throws RegisterException {
        if (!registerDTO.getPassword().equals(registerDTO.getRe_password())) {
            throw new RegisterException("Passwords do not match");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RegisterException("User already exists");
        }

    }

    private void validateUser(LoginRequestDTO loginRequestDTO) throws LoginException {
        Optional<User> user = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (user.isEmpty()) {
            throw new LoginException("User not found");
        }
    }
}
