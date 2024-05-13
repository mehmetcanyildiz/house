package com.apartment.house.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordResetClientRequestDTO {

  @Email(message = "Email is invalid")
  @NotEmpty(message = "Email is required")
  @NotBlank(message = "Email is required")
  private String email;
}
