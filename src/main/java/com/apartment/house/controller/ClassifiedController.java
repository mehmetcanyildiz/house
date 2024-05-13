package com.apartment.house.controller;

import com.apartment.house.dto.classified.CreateRequestDTO;
import com.apartment.house.dto.classified.CreateResponseDTO;
import com.apartment.house.service.ClassifiedService;
import com.apartment.house.service.LoggerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classified")
@AllArgsConstructor
public class ClassifiedController {

  private final ClassifiedService classifiedService;
  private final LoggerService loggerService;

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody @Valid CreateRequestDTO requestDTO) {
    CreateResponseDTO response = classifiedService.create(requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }
}
