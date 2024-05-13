package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum ClassifiedStatusEnum {
  ACTIVE(1),
  PASSIVE(0);

  private final Integer status;

  ClassifiedStatusEnum(Integer status) {
    this.status = status;
  }
}
