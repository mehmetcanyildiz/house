package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateNameEnum {

  ACTIVATION_EMAIL("activation_account"),

  CLASSIFIED_CREATED("classified_created"),

  CLASSIFIED_FAVORITE("classified_favorite"),

  RESET_PASSWORD("reset_password");

  private final String value;

  EmailTemplateNameEnum(String value) {
    this.value = value;
  }
}
