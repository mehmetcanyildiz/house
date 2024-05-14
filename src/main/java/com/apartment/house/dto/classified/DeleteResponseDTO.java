package com.apartment.house.dto.classified;


import lombok.Data;

@Data
public class DeleteResponseDTO {

  private boolean status = false;

  private String id;

  private String message;
}
