package com.apartment.house.dto.classified;


import com.apartment.house.enums.ClassifiedCategoryEnum;
import com.apartment.house.enums.ClassifiedTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRequestDTO {

  @NotNull(message = "Title is required")
  private String title;
  @NotBlank(message = "Description is required")
  private String description;
  @NotNull(message = "Price is required")
  private Double price;
  @NotNull(message = "Type is required")
  private ClassifiedTypeEnum type;
  @NotNull(message = "Category is required")
  private ClassifiedCategoryEnum category;
  @NotNull(message = "Room number is required")
  private Integer roomNumber;
  @NotNull(message = "Gross area is required")
  private Integer grossArea;
  @NotNull(message = "Net area is required")
  private Integer netArea;
  @NotNull(message = "Building age is required")
  private Integer buildingAge;
  @NotNull(message = "Floor location is required")
  private Integer floorLocation;
  @NotNull(message = "Total floor is required")
  private Integer totalFloor;
  @NotNull(message = "Furnished is required")
  private Boolean isFurnished;
}
