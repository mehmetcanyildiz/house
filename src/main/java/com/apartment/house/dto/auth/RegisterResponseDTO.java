package com.apartment.house.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class RegisterResponseDTO {

  private boolean status = false;
  private String message;
  private List<String> errors;
}
