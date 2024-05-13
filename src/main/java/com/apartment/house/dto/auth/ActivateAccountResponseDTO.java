package com.apartment.house.dto.auth;

import lombok.Data;

@Data
public class ActivateAccountResponseDTO {

  private boolean status = false;
  private String message;
}
