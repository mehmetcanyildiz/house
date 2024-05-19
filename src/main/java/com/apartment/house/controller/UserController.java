package com.apartment.house.controller;

import com.apartment.house.dto.user.UserFavoriteDeleteRequestDTO;
import com.apartment.house.dto.user.UserFavoriteRequestDTO;
import com.apartment.house.dto.user.UserFavoriteResponseDTO;
import com.apartment.house.dto.user.UserFavoritesResponseDTO;
import com.apartment.house.dto.user.UserUpdateRequestDTO;
import com.apartment.house.dto.user.UserUpdateResponseDTO;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.service.AuthService;
import com.apartment.house.service.ClassifiedService;
import com.apartment.house.service.LoggerService;
import com.apartment.house.service.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "User", description = "API Endpoints for Users")
public class UserController {

  private final UserService userService;
  private final LoggerService loggerService;
  private final AuthService authService;
  private final ClassifiedService classifiedService;

  @Operation(summary = "Update", description = "Update a user", tags = {"User"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @PutMapping("/update")
  public ResponseEntity<?> update(@RequestBody @Valid UserUpdateRequestDTO requestDTO) {
    UserModel user = authService.getAuthUser();
    UserUpdateResponseDTO response = userService.update(user, requestDTO);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Add Favorite", description = "Add a favorite", tags = {
      "User"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Favorite added"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @PostMapping("/favorite/add")
  public ResponseEntity<?> addFavorite(@RequestBody @Valid UserFavoriteRequestDTO requestDTO)
      throws MessagingException {
    UserModel user = authService.getAuthUser();
    ClassifiedModel classified = classifiedService.findClassifiedById(requestDTO.getClassifiedId());
    if (userService.isFavorite(user, classified)) {
      return ResponseEntity.badRequest().body("Already added to favorites");
    }
    UserFavoriteResponseDTO response = userService.addFavorite(user, classified);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete Favorite", description = "Delete a favorite", tags = {
      "User"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Favorite deleted"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @DeleteMapping("/favorite/delete")
  public ResponseEntity<?> deleteFavorite(
      @RequestBody @Valid UserFavoriteDeleteRequestDTO requestDTO) {
    UserModel user = authService.getAuthUser();
    ClassifiedModel classified = classifiedService.findClassifiedById(requestDTO.getClassifiedId());
    UserFavoriteResponseDTO response = userService.deleteFavorite(user, classified);
    loggerService.logInfo(response.getId() + " => " + response.getMessage());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Favorites", description = "Get all favorites", tags = {
      "User"}, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Favorites retrieved"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")})
  @GetMapping("/favorites")
  public ResponseEntity<?> getFavorites() {
    UserModel user = authService.getAuthUser();

    List<UserFavoritesResponseDTO> favorites = userService.getFavorites(user);

    return ResponseEntity.ok(favorites);
  }

}
