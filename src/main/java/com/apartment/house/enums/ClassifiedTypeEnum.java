package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum ClassifiedTypeEnum {

  SALE("Sale"),

  RENT("Rent");

  private final String type;

  ClassifiedTypeEnum(String type) {
    this.type = type;
  }
}
