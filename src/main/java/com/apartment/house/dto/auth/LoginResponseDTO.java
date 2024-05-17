package com.apartment.house.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {

  private boolean status = false;

  private String message;

  private String uid;

  private String email;

  private String firstName;

  private String lastName;

  private String phone;

  private String accessToken;
}
