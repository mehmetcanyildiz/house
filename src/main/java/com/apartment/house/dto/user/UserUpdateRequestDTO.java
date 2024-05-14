package com.apartment.house.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequestDTO {

  @NotEmpty(message = "Firstname is required")
  @NotBlank(message = "Firstname is required")
  @Size(min = 2, message = "Firstname must be at least 2 characters")
  private String firstname;

  @NotEmpty(message = "Lastname is required")
  @NotBlank(message = "Lastname is required")
  @Size(min = 2, message = "Lastname must be at least 2 characters")
  private String lastname;

  @Size(min = 10, message = "Phone must be at least 10 characters")
  @NotEmpty(message = "Phone is required")
  @NotBlank(message = "Phone is required")
  private String phone;
}
