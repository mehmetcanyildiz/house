package com.apartment.house.model.dto.auth;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private boolean status = false;
    private String message;
}
