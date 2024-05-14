package com.apartment.house.dto.auth;

import java.util.List;
import lombok.Data;

@Data
public class RegisterResponseDTO {

  private boolean status = false;

  private String message;

  private List<String> errors;
}
