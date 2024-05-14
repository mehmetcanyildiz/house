package com.apartment.house.dto.user;

import lombok.Data;

@Data
public class UserFavoriteResponseDTO {

  private boolean status = false;
  private String message = "";
  private String id;
}
