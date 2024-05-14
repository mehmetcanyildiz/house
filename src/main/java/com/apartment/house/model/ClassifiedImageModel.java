package com.apartment.house.model;

import com.apartment.house.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "classified_image")
public class ClassifiedImageModel extends BaseModel {

  @ManyToOne
  @JoinColumn(name = "classified_id", nullable = false, updatable = false)
  private ClassifiedModel classified;

  @Column(name = "path", nullable = false)
  private String path;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "status", nullable = false)
  private StatusEnum status;
}
