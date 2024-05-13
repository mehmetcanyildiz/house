package com.apartment.house.controller;

import com.apartment.house.dto.HomeResponseDTO;
import com.apartment.house.service.LoggerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Site", description = "API Endpoints for Site")
public class SiteController {

  private final LoggerService loggerService;

  @Operation(summary = "Home", description = "Health check", responses = {
      @ApiResponse(responseCode = "200", description = "Health check successful"),
      @ApiResponse(responseCode = "400", description = "Bad request"),
  })
  @GetMapping("/")
  public ResponseEntity<?> home() {
    HomeResponseDTO response = new HomeResponseDTO();
    response.setStatus(true);
    response.setMessage("Welcome to the Apartment House API");
    loggerService.logInfo("Health check => Welcome to the Apartment House API");

    return ResponseEntity.ok(response);
  }
}
