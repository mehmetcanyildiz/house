package com.apartment.house.model.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
