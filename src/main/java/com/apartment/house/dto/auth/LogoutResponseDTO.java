package com.apartment.house.dto.auth;

import lombok.Data;

@Data
public class LogoutResponseDTO {

  private boolean status = false;
  private String email;
  private String message;
}
