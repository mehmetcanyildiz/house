package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum TokenTypeEnum {

  ACTIVATION("activation_account"),

  RESET("reset_password");

  private final String type;

  TokenTypeEnum(String type) {
    this.type = type;
  }
}
