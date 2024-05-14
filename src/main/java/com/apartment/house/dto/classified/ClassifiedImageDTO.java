package com.apartment.house.dto.classified;

import java.util.Date;
import lombok.Data;

@Data
public class ClassifiedImageDTO {

  private String name;

  private String path;

  private Date createdAt;

  private Date updatedAt;
}