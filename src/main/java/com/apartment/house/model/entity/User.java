package com.apartment.house.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

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
