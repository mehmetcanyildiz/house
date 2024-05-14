package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum ClassifiedStatusEnum {

  ACTIVE("active"),

  PASSIVE("passive");

  private final String status;

  ClassifiedStatusEnum(String status) {
    this.status = status;
  }
}
