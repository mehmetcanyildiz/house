package com.apartment.house.model;

import com.apartment.house.enums.TokenTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "token")
public class TokenModel extends BaseModel {

  @Column(name = "type", nullable = false, updatable = false)
  private TokenTypeEnum type;

  private String token;

  @Column(name = "expires_at", nullable = false, updatable = false)
  private LocalDateTime expiresAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private UserModel user;
}
