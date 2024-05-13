package com.apartment.house.model;

import com.apartment.house.enums.RoleEnum;
import com.apartment.house.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Entity
@Table(name = "users", indexes = {@Index(name = "idx_email_phone", columnList = "email, phone")})
@EqualsAndHashCode(callSuper = true)
public class UserModel extends BaseModel implements UserDetails {

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

  @Column(name = "role", nullable = false)
  private RoleEnum role;

  @Column(name = "status", nullable = false)
  private StatusEnum status = StatusEnum.INACTIVE;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return getStatus() == StatusEnum.ACTIVE;
  }
}
