package com.apartment.house.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "user_favorite")
public class UserFavoriteModel extends BaseModel {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private UserModel user;

  @ManyToOne
  @JoinColumn(name = "classified_id", nullable = false, updatable = false)
  private ClassifiedModel classified;
}
