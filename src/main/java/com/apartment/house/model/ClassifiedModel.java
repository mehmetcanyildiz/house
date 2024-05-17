package com.apartment.house.model;

import com.apartment.house.enums.ClassifiedCategoryEnum;
import com.apartment.house.enums.ClassifiedStatusEnum;
import com.apartment.house.enums.ClassifiedTypeEnum;
import com.apartment.house.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "classified")
public class ClassifiedModel extends BaseModel {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private UserModel user;

  @Column(name = "title", nullable = false, length = 60)
  private String title;

  @Column(name = "slug", nullable = false)
  private String slug;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "type", nullable = false)
  private ClassifiedTypeEnum type;

  @Column(name = "category", nullable = false)
  private ClassifiedCategoryEnum category;

  @Column(name = "room_number", nullable = false)
  private Integer roomNumber;

  @Column(name = "living_room_number", nullable = false)
  private Integer livingRoomNumber;

  @Column(name = "gross_area", nullable = false)
  private Integer grossArea;

  @Column(name = "net_area", nullable = false)
  private Integer netArea;

  @Column(name = "building_age", nullable = false)
  private Integer buildingAge;

  @Column(name = "floor_location", nullable = false)
  private Integer floorLocation;

  @Column(name = "total_floor", nullable = false)
  private Integer totalFloor;

  @Column(name = "is_furnished", nullable = false)
  private Boolean isFurnished;

  @Column(name = "classified_status", nullable = false)
  private ClassifiedStatusEnum classifiedStatus;

  @Column(name = "status", nullable = false)
  private StatusEnum status;
}
