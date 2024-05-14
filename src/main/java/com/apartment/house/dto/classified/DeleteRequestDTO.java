package com.apartment.house.dto.classified;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteRequestDTO {

  @NotNull(message = "Id is required")
  private String id;
}