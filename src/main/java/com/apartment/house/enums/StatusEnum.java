package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

  ACTIVE(10),

  INACTIVE(5),

  DELETED(0);

  private final int status;

  StatusEnum(int status) {
    this.status = status;
  }
}
