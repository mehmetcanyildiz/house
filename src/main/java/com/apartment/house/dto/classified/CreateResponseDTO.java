package com.apartment.house.dto.classified;


import lombok.Data;

@Data
public class CreateResponseDTO {

  private boolean status = false;

  private String slug;

  private String message;

  private String id;
}
