package com.apartment.house.dto.classified;


import lombok.Data;

@Data
public class UpdateResponseDTO {

  private boolean status = false;

  private String message;

  private String id;

  private String slug;
}
