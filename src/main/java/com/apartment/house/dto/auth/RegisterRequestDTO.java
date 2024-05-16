package com.apartment.house.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

  @NotEmpty(message = "Email is required")
  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;

  @NotEmpty(message = "Password is required")
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  @Size(max = 20, message = "Password must be at most 20 characters")
  private String password;

  @NotEmpty(message = "Re-password is required")
  @NotBlank(message = "Re-password is required")
  @Size(min = 6, message = "Re-password must be at least 6 characters")
  @Size(max = 20, message = "Re-password must be at most 20 characters")
  private String re_password;

  @NotEmpty(message = "Firstname is required")
  @NotBlank(message = "Firstname is required")
  @Size(min = 2, message = "Firstname must be at least 2 characters")
  private String firstname;

  @NotEmpty(message = "Lastname is required")
  @NotBlank(message = "Lastname is required")
  @Size(min = 2, message = "Lastname must be at least 2 characters")
  private String lastname;

  @Size(min = 10,max = 10, message = "Phone must be 10 characters")
  @NotEmpty(message = "Phone is required")
  @NotBlank(message = "Phone is required")
  private String phone;
}
