package com.apartment.house.model.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateNameEnum {
    ACTIVATION_EMAIL("activation_account"),;

    private final String value;

    EmailTemplateNameEnum(String value) {
        this.value = value;
    }

}