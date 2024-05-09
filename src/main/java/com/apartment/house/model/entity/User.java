package com.apartment.house.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@Entity
@Table(name = "users", indexes = {@Index(name = "idx_email_phone", columnList = "email, phone")})
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

}
