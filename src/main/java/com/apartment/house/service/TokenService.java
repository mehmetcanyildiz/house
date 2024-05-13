package com.apartment.house.service;

import com.apartment.house.enums.TokenTypeEnum;
import com.apartment.house.model.TokenModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.TokenRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public void save(TokenModel token) {
    tokenRepository.save(token);
  }

  public Optional<TokenModel> findByTypeAndUser(TokenTypeEnum type, UserModel user) {
    return tokenRepository.findFirstByTypeAndUserOrderByCreatedAtDesc(type, user);
  }

  public TokenModel findByToken(String token) {
    return tokenRepository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Token not found"));
  }


  public TokenModel createTokenModel(UserModel user, TokenTypeEnum type, String token) {
    TokenModel tokenModel = new TokenModel();
    tokenModel.setType(type);
    tokenModel.setToken(token);
    tokenModel.setUser(user);
    tokenModel.setExpiresAt(LocalDateTime.now().plusMinutes(15));

    return tokenModel;
  }

  public String generateActivationToken(int length) {
    String numbers = "0123456789";
    StringBuilder token = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int index = secureRandom.nextInt(numbers.length());
      token.append(numbers.charAt(index));
    }

    return token.toString();
  }

  public String encodeBase64(String token) {
    return Base64.getEncoder().encodeToString(token.getBytes());
  }

  public String decodeBase64(String token) {
    return new String(Base64.getDecoder().decode(token));
  }
}
