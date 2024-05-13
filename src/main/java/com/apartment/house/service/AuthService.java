package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
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
import com.apartment.house.enums.EmailTemplateNameEnum;
import com.apartment.house.enums.StatusEnum;
import com.apartment.house.enums.TokenTypeEnum;
import com.apartment.house.model.TokenModel;
import com.apartment.house.model.UserModel;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

  private final ApplicationConfig applicationConfig;
  private final AuthenticationManager autenticationManager;
  private final EmailService emailService;
  private final JwtService jwtService;
  private final UserService userService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

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
      sendValidationEmail(user);
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
  public ActivateAccountResponseDTO activateAccount(ActivateAccountRequestDTO activateDTO)
      throws MessagingException {
    String decodedToken = tokenService.decodeBase64(activateDTO.getToken());
    activateDTO.setToken(decodedToken);

    TokenModel savedTokenModel = tokenService.findByToken(activateDTO.getToken());
    if (LocalDateTime.now().isAfter(savedTokenModel.getExpiresAt())) {
      sendValidationEmail(savedTokenModel.getUser());
      throw new RuntimeException(
          "Activation Token has expired. A new token has been sent to your email");
    }

    UserModel user = userService.findUserById(savedTokenModel.getUser().getId());

    if (user.getStatus() == StatusEnum.ACTIVE) {
      throw new RuntimeException("Account already activated");
    }

    user.setStatus(StatusEnum.ACTIVE);
    userService.save(user);

    ActivateAccountResponseDTO response = new ActivateAccountResponseDTO();
    response.setStatus(true);
    response.setEmail(user.getEmail());
    response.setMessage("Account activated successfully");
    return response;
  }

  public PasswordResetClientResponseDTO passwordResetClient(
      PasswordResetClientRequestDTO resetRequestDTO) throws MessagingException {
    PasswordResetClientResponseDTO response = new PasswordResetClientResponseDTO();

    UserModel user = userService.findUserByEmail(resetRequestDTO.getEmail());
    sendResetPasswordEmail(user);
    response.setStatus(true);
    response.setEmail(user.getEmail());
    response.setMessage("Password reset link sent to your email");

    return response;
  }

  public PasswordResetResponseDTO passwordReset(PasswordResetRequestDTO resetDTO)
      throws MessagingException {
    String decodedToken = tokenService.decodeBase64(resetDTO.getToken());
    resetDTO.setToken(decodedToken);

    TokenModel savedTokenModel = tokenService.findByToken(resetDTO.getToken());
    if (LocalDateTime.now().isAfter(savedTokenModel.getExpiresAt())) {
      sendResetPasswordEmail(savedTokenModel.getUser());
      throw new RuntimeException(
          "Reset Password Token has expired. A new token has been sent to your email");
    }

    UserModel user = userService.findUserById(savedTokenModel.getUser().getId());

    if (user.getStatus() != StatusEnum.ACTIVE) {
      throw new RuntimeException("Account is not active.");
    }

    if (!resetDTO.getPassword().equals(resetDTO.getRe_password())) {
      throw new RuntimeException("Passwords do not match");
    }

    user.setPassword(passwordEncoder.encode(resetDTO.getPassword()));
    userService.save(user);

    PasswordResetResponseDTO response = new PasswordResetResponseDTO();
    response.setStatus(true);
    response.setEmail(user.getEmail());
    response.setMessage("Password reset successfully");
    return response;
  }

  private void sendValidationEmail(UserModel user) throws MessagingException {
    Optional<TokenModel> tokenModel = tokenService.findByTypeAndUser(
        TokenTypeEnum.ACTIVATION, user);
    if (tokenModel.isPresent() && !LocalDateTime.now().isAfter(tokenModel.get().getExpiresAt())) {
      throw new RuntimeException(
          "You have an activation token that is still valid. Please check your email for the link");
    }

    var newToken = generateAndSaveActivationToken(user, TokenTypeEnum.ACTIVATION, 6);
    var baseUrl = applicationConfig.baseUrl;
    var encodedToken = tokenService.encodeBase64((String) newToken);
    var activationUrl = baseUrl + "/auth/activate?token=" + encodedToken;

    emailService.sendEmail(user.getEmail(), user.getFirstName(),
                           EmailTemplateNameEnum.ACTIVATION_EMAIL, activationUrl, (String) newToken,
                           "Account activation"
    );
  }

  private void sendResetPasswordEmail(UserModel user) throws MessagingException {
    Optional<TokenModel> tokenModel = tokenService.findByTypeAndUser(
        TokenTypeEnum.RESET, user);
    if (tokenModel.isPresent() && !LocalDateTime.now().isAfter(tokenModel.get().getExpiresAt())) {
      throw new RuntimeException(
          "You have an reset password token that is still valid. Please check your email for the link");
    }

    var newToken = generateAndSaveActivationToken(user, TokenTypeEnum.RESET, 8);
    var baseUrl = applicationConfig.baseUrl;
    var encodedToken = tokenService.encodeBase64((String) newToken);
    var activationUrl = baseUrl + "/auth/reset-password?token=" + encodedToken;

    emailService.sendEmail(user.getEmail(), user.getFirstName(),
                           EmailTemplateNameEnum.RESET_PASSWORD, activationUrl, (String) newToken,
                           "Reset Password"
    );
  }

  private Object generateAndSaveActivationToken(UserModel user, TokenTypeEnum type, int length) {
    String generatedToken = tokenService.generateActivationToken(length);
    TokenModel tokenModel = tokenService.createTokenModel(user, type, generatedToken);
    tokenService.save(tokenModel);

    return generatedToken;
  }

  public LogoutResponseDTO logout(LogoutRequestDTO logoutRequestDTO) {
    LogoutResponseDTO response = new LogoutResponseDTO();
    String userEmail = jwtService.extractUsername(logoutRequestDTO.getToken());
    jwtService.addBlackListToken(logoutRequestDTO);
    response.setStatus(true);
    response.setEmail(userEmail);
    response.setMessage("User logged out successfully");

    return response;
  }
}
