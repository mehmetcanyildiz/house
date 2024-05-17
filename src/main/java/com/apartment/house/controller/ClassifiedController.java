package com.apartment.house.controller;

import com.apartment.house.dto.classified.ClassifiedDTO;
import com.apartment.house.dto.classified.CreateRequestDTO;
import com.apartment.house.dto.classified.CreateResponseDTO;
import com.apartment.house.dto.classified.DeleteResponseDTO;
import com.apartment.house.dto.classified.UpdateRequestDTO;
import com.apartment.house.dto.classified.UpdateResponseDTO;
import com.apartment.house.service.ClassifiedService;
import com.apartment.house.service.LoggerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
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

  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Create", description = "Create a classified", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classified created"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @PostMapping("/create")
  public ResponseEntity<?> create(@ModelAttribute @Valid CreateRequestDTO requestDTO)
      throws MessagingException {
    CreateResponseDTO response = classifiedService.create(requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Update", description = "Update a classified", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classified updated"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable String id,
      @ModelAttribute @Valid UpdateRequestDTO requestDTO) {
    UpdateResponseDTO response = classifiedService.update(id, requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete", description = "Delete a classified", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classified deleted"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    DeleteResponseDTO deleteResponseDTO = classifiedService.delete(id);
    loggerService.logInfo(deleteResponseDTO.getId() + " => Classified deleted");

    return ResponseEntity.ok(deleteResponseDTO);
  }

  @Operation(summary = "Delete Image", description = "Delete an image", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image deleted"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/delete/image/{id}")
  public ResponseEntity<?> deleteImage(@PathVariable String id) {
    DeleteResponseDTO deleteResponseDTO = classifiedService.deleteImage(id);
    loggerService.logInfo(deleteResponseDTO.getId() + " => Image deleted");

    return ResponseEntity.ok(deleteResponseDTO);
  }

  @Operation(summary = "Get All", description = "Get all classifieds", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classifieds retrieved"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @GetMapping("/all")
  public ResponseEntity<?> getItems() {

    List<ClassifiedDTO> classifieds = classifiedService.getItems();
    return ResponseEntity.ok(classifieds);
  }

  @Operation(summary = "Get By Id", description = "Get a classified by id", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classified retrieved"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @GetMapping("/get/{id}")
  public ResponseEntity<?> getClassifiedById(@PathVariable String id) {
    ClassifiedDTO classified = classifiedService.getClassifiedById(id);
    return ResponseEntity.ok(classified);
  }

  @Operation(summary = "Get By User Id", description = "Get classifieds by user id", tags = {
      "Classified"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Classifieds retrieved"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/user/{id}")
  public ResponseEntity<?> getClassifiedByUserId(@PathVariable String id) {
    List<ClassifiedDTO> classifieds = classifiedService.getClassifiedByUserId(id);
    return ResponseEntity.ok(classifieds);
  }
}
