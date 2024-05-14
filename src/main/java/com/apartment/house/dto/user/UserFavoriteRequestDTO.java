package com.apartment.house.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserFavoriteRequestDTO {

  @NotNull(message = "Classified ID is required")
  private String classifiedId;
}
