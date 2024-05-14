package com.apartment.house.dto.classified;


import com.apartment.house.enums.ClassifiedCategoryEnum;
import com.apartment.house.enums.ClassifiedTypeEnum;

public interface ClassifiedRequestDTO {

  String getTitle();

  String getDescription();

  Double getPrice();

  ClassifiedTypeEnum getType();

  ClassifiedCategoryEnum getCategory();

  Integer getRoomNumber();

  Integer getLivingRoomNumber();

  Integer getGrossArea();

  Integer getNetArea();

  Integer getBuildingAge();

  Integer getFloorLocation();

  Integer getTotalFloor();

  Boolean getIsFurnished();
}
