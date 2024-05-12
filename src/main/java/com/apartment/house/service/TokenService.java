package com.apartment.house.service;

import com.apartment.house.model.TokenModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public void save(TokenModel token) {
    tokenRepository.save(token);
  }

  public TokenModel findByUser(UserModel user) {
    return tokenRepository.findByUser(user).stream().findFirst()
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  public TokenModel findByToken(String token) {
    return tokenRepository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Token not found"));
  }


  public TokenModel createTokenModel(UserModel user, String token) {
    TokenModel tokenModel = new TokenModel();
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

}
