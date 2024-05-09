package com.apartment.house.model.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String re_password;
    private String firstname;
    private String lastname;
    private String phone;
}
