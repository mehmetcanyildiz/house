package com.apartment.house.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

  ADMIN("ADMIN"),

  USER("USER");

  private final String role;

  RoleEnum(String role) {
    this.role = role;
  }
}
