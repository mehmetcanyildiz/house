package com.apartment.house.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {

  private boolean status = false;
  private String message;
  private String token;
}
