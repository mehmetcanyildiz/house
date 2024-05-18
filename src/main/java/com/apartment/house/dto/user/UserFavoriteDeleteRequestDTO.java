package com.apartment.house.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserFavoriteDeleteRequestDTO {

  @NotNull
  private String classifiedId;
}
