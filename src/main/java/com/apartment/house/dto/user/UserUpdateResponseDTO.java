package com.apartment.house.dto.user;

import lombok.Data;

@Data
public class UserUpdateResponseDTO {

  private boolean status = false;
  private String message = "";
  private String id;
}
