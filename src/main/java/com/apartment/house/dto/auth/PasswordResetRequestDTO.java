package com.apartment.house.dto.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {

  @NotEmpty(message = "Token is required")
  @NotBlank(message = "Token is required")
  private String token;

  @NotEmpty(message = "Password is required")
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  @Size(max = 20, message = "Password must be at most 20 characters")
  private String password;

  @NotEmpty(message = "Confirm password is required")
  @NotBlank(message = "Confirm password is required")
  @Size(min = 6, message = "Re-Password must be at least 6 characters")
  @Size(max = 20, message = "Re-Password must be at most 20 characters")
  private String re_password;

}
