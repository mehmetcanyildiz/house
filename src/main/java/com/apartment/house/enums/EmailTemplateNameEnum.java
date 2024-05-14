package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateNameEnum {

  ACTIVATION_EMAIL("activation_account"),

  RESET_PASSWORD("reset_password");

  private final String value;

  EmailTemplateNameEnum(String value) {
    this.value = value;
  }
}
