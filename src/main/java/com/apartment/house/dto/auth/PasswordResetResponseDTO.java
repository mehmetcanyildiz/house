package com.apartment.house.dto.auth;

import lombok.Data;

@Data
public class PasswordResetResponseDTO {

  private boolean status = false;
  private String message;
}
