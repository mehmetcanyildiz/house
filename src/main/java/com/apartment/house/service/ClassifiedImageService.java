package com.apartment.house.service;

import com.apartment.house.dto.classified.ClassifiedImageDTO;
import com.apartment.house.dto.classified.CreateImageRequestDTO;
import com.apartment.house.dto.classified.CreateImageResponseDTO;
import com.apartment.house.enums.StatusEnum;
import com.apartment.house.model.ClassifiedImageModel;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.repository.ClassifiedImageRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassifiedImageService {

  private final ClassifiedImageRepository classifiedImageRepository;
  private final FileStorageService fileStorageService;

  public CreateImageResponseDTO uploadImages(CreateImageRequestDTO requestDTO) {
    List<ClassifiedImageModel> classifiedImages = new ArrayList<>();
    if (requestDTO.getImages() == null || requestDTO.getImages().isEmpty()) {
      CreateImageResponseDTO responseDTO = new CreateImageResponseDTO();
      responseDTO.setStatus(true);
      responseDTO.setMessage("Image upload is not found");
      return responseDTO;
    }
    requestDTO.getImages().forEach(image -> {
      String newFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

      String path = fileStorageService.storeFile(image, newFileName);

      ClassifiedImageModel classifiedImage = new ClassifiedImageModel();
      classifiedImage.setClassified(requestDTO.getClassified());
      classifiedImage.setPath(path);
      classifiedImage.setName(newFileName);
      classifiedImage.setStatus(StatusEnum.ACTIVE);

      classifiedImages.add(classifiedImage);
    });

    classifiedImageRepository.saveAll(classifiedImages);

    CreateImageResponseDTO responseDTO = new CreateImageResponseDTO();
    responseDTO.setStatus(true);
    responseDTO.setMessage("Images uploaded successfully");

    return responseDTO;
  }

  public List<ClassifiedImageDTO> getClassifiedImages(ClassifiedModel classifiedModel) {
    List<ClassifiedImageModel> classifiedImages = classifiedImageRepository.findByClassifiedAndStatus(
        classifiedModel, StatusEnum.ACTIVE);
    List<ClassifiedImageDTO> classifiedImageDTOS = new ArrayList<>();

    classifiedImages.forEach(image -> {
      ClassifiedImageDTO classifiedImageDTO = new ClassifiedImageDTO();

      classifiedImageDTO.setId(image.getId());
      classifiedImageDTO.setName(image.getName());
      classifiedImageDTO.setPath(image.getPath());
      classifiedImageDTO.setCreatedAt(image.getCreatedAt());
      classifiedImageDTO.setUpdatedAt(image.getUpdatedAt());

      classifiedImageDTOS.add(classifiedImageDTO);
    });

    return classifiedImageDTOS;
  }

  public void deleteImages(ClassifiedModel classifiedModel) {
    List<ClassifiedImageModel> classifiedImages = classifiedImageRepository.findByClassified(
        classifiedModel);

    classifiedImages.forEach(image -> {
      image.setStatus(StatusEnum.DELETED);
    });
    classifiedImageRepository.saveAll(classifiedImages);
  }

  public void deleteImage(String id) {
    ClassifiedImageModel classifiedImage = classifiedImageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Image not found"));

    classifiedImage.setStatus(StatusEnum.DELETED);
    classifiedImageRepository.save(classifiedImage);
  }

  public ClassifiedImageModel findById(String id) {
    return classifiedImageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Image not found"));
  }

  public void findLastImageDelete(String id) {
    ClassifiedImageModel classifiedImage = findById(id);
    imageCountCheck(classifiedImage);
  }

  private void imageCountCheck(ClassifiedImageModel classifiedImage) {
    ClassifiedModel classifiedModel = classifiedImage.getClassified();
    List<ClassifiedImageModel> classifiedImages = classifiedImageRepository.findByClassified(
        classifiedModel);

    if (classifiedImages.size() == 1) {
      throw new RuntimeException("You can not delete the last image");
    }
  }
}
