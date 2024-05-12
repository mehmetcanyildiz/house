package com.apartment.house.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodesEnum {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),

    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Current password is incorrect"),

    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),

    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),

    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),

    BAD_CREDENTIALS(304, HttpStatus.FORBIDDEN, "Login and / or password are incorrect");

    private final int code;

    private final String description;

    private final HttpStatus httpStatus;

    BusinessErrorCodesEnum(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
