package com.apartment.house.model.entity;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Token extends BaseEntity
{
    private String token;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false, updatable = false)
    private User user;
}
