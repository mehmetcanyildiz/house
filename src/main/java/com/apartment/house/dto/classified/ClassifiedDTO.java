package com.apartment.house.dto.classified;

import com.apartment.house.enums.ClassifiedCategoryEnum;
import com.apartment.house.enums.ClassifiedStatusEnum;
import com.apartment.house.enums.ClassifiedTypeEnum;
import java.util.List;
import lombok.Data;

@Data
public class ClassifiedDTO {

  private String title;

  private String slug;

  private String description;

  private Double price;

  private ClassifiedTypeEnum type;

  private ClassifiedCategoryEnum category;

  private Integer roomNumber;

  private Integer livingRoomNumber;

  private Integer grossArea;

  private Integer netArea;

  private Integer buildingAge;

  private Integer floorLocation;

  private Integer totalFloor;

  private Boolean isFurnished;

  private ClassifiedStatusEnum classifiedStatus;

  private List<ClassifiedImageDTO> images;
}