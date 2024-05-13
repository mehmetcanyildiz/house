package com.apartment.house.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ActivateAccountRequestDTO {

  @NotEmpty(message = "Token is required")
  @NotBlank(message = "Token is required")
  private String token;
}
