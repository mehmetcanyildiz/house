package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.dto.auth.LoginRequestDTO;
import com.apartment.house.dto.auth.LoginResponseDTO;
import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.dto.auth.RegisterResponseDTO;
import com.apartment.house.model.TokenModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.enums.EmailTemplateNameEnum;
import com.apartment.house.enums.StatusEnum;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthService {

  private final ApplicationConfig applicationConfig;
  private final AuthenticationManager autenticationManager;
  private final EmailService emailService;
  private final JwtService jwtService;
  private final UserService userService;
  private final TokenService tokenService;

  @SneakyThrows
  public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws ExpiredJwtException {
    LoginResponseDTO response = new LoginResponseDTO();
    userService.validateUserByEmail(loginRequestDTO.getEmail());
    var auth = autenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.getEmail(),
            loginRequestDTO.getPassword()
        ));
    var claims = new HashMap<String, Object>();
    var user = ((UserModel) auth.getPrincipal());
    if (user.getStatus() != StatusEnum.ACTIVE) {
      TokenModel tokenModel = tokenService.findByUser(user);
      if (LocalDateTime.now().isAfter(tokenModel.getExpiresAt())) {
        sendValidationEmail(tokenModel.getUser());
        throw new RuntimeException(
            "Activation Token has expired. A new token has been sent to your email");
      }
      throw new Exception("Account not activated. Please check your email for activation link");
    }
    claims.put("firstName", user.getFirstName());
    claims.put("lastname", user.getLastName());
    claims.put("email", user.getEmail());
    var jwtToken = jwtService.generateToken(claims, user);
    response.setStatus(true);
    response.setToken(jwtToken);
    response.setMessage("User logged in successfully");

    return response;
  }

  @Transactional
  public RegisterResponseDTO register(RegisterRequestDTO registerDTO) throws Exception {
    RegisterResponseDTO response = new RegisterResponseDTO();
    userService.validateRegister(registerDTO);
    UserModel user = userService.register(registerDTO);
    sendValidationEmail(user);
    response.setStatus(true);
    response.setMessage("User registered successfully");

    return response;
  }

  @Transactional
  public void activateAccount(String token) throws MessagingException {
    TokenModel savedTokenModel = tokenService.findByToken(token);
    if (LocalDateTime.now().isAfter(savedTokenModel.getExpiresAt())) {
      sendValidationEmail(savedTokenModel.getUser());
      throw new RuntimeException(
          "Activation Token has expired. A new token has been sent to your email");
    }
    UserModel user = userService.findUserById(savedTokenModel.getUser().getId());
    user.setStatus(StatusEnum.ACTIVE);
    userService.save(user);
  }

  private void sendValidationEmail(UserModel user) throws MessagingException {
    var newToken = generateAndSaveActivationToken(user);
    var activationUrl = applicationConfig.baseUrl + "/auth/activate?token=" + newToken;
    emailService.sendEmail(
        user.getEmail(),
        user.getFirstName(),
        EmailTemplateNameEnum.ACTIVATION_EMAIL,
        activationUrl, (String) newToken,
        "Account activation"
    );
  }

  private Object generateAndSaveActivationToken(UserModel user) {
    String generatedToken = tokenService.generateActivationToken(6);
    TokenModel tokenModel = tokenService.createTokenModel(user, generatedToken);
    tokenService.save(tokenModel);

    return generatedToken;
  }


}
