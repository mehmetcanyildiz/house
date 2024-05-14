package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum ClassifiedCategoryEnum {

  HOUSE("House"),

  APARTMENT("Apartment"),

  RESIDENCE("Residence"),

  DETACHED_HOUSE("Detached House"),

  VILLA("Villa");

  private final String category;

  ClassifiedCategoryEnum(String category) {
    this.category = category;
  }
}
