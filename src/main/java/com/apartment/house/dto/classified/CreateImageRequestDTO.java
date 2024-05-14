package com.apartment.house.dto.classified;

import com.apartment.house.model.ClassifiedModel;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateImageRequestDTO {

  @NotNull(message = "Images are required")
  private List<MultipartFile> images;

  @NotNull(message = "Classified is required")
  private ClassifiedModel classified;
}