package com.apartment.house.controller;

import com.apartment.house.dto.classified.ClassifiedDTO;
import com.apartment.house.dto.classified.CreateRequestDTO;
import com.apartment.house.dto.classified.CreateResponseDTO;
import com.apartment.house.dto.classified.DeleteResponseDTO;
import com.apartment.house.dto.classified.UpdateRequestDTO;
import com.apartment.house.dto.classified.UpdateResponseDTO;
import com.apartment.house.service.ClassifiedService;
import com.apartment.house.service.LoggerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classified")
@RequiredArgsConstructor
@Tag(name = "Classified", description = "API Endpoints for Classifieds")
public class ClassifiedController {

  private final ClassifiedService classifiedService;
  private final LoggerService loggerService;

  @PostMapping("/create")
  public ResponseEntity<?> create(@ModelAttribute @Valid CreateRequestDTO requestDTO) {
    CreateResponseDTO response = classifiedService.create(requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable String id,
      @ModelAttribute @Valid UpdateRequestDTO requestDTO) {
    UpdateResponseDTO response = classifiedService.update(id, requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    DeleteResponseDTO deleteResponseDTO = classifiedService.delete(id);
    loggerService.logInfo(deleteResponseDTO.getId() + " => Classified deleted");

    return ResponseEntity.ok(deleteResponseDTO);
  }

  @DeleteMapping("/delete/image/{id}")
  public ResponseEntity<?> deleteImage(@PathVariable String id) {
    DeleteResponseDTO deleteResponseDTO = classifiedService.deleteImage(id);
    loggerService.logInfo(deleteResponseDTO.getId() + " => Image deleted");

    return ResponseEntity.ok(deleteResponseDTO);
  }

  @GetMapping("/all")
  public ResponseEntity<?> getItems() {

    List<ClassifiedDTO> classifieds = classifiedService.getItems();
    return ResponseEntity.ok(classifieds);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<?> getClassifiedById(@PathVariable String id) {
    ClassifiedDTO classified = classifiedService.getClassifiedById(id);
    return ResponseEntity.ok(classified);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<?> getClassifiedByUserId(@PathVariable String id) {
    List<ClassifiedDTO> classifieds = classifiedService.getClassifiedByUserId(id);
    return ResponseEntity.ok(classifieds);
  }
}
