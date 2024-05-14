package com.apartment.house.dto.user;

import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserFavoriteModel;
import com.apartment.house.model.UserModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class UserFavoritesResponseDTO {

  private ClassifiedModel classified;
  private UserModel user;
  private Date createdAt;
  private Date updatedAt;

  public static List<UserFavoritesResponseDTO> convert(List<UserFavoriteModel> favorites) {
    List<UserFavoritesResponseDTO> response = new ArrayList<>();
    favorites.forEach(favorite -> {
      UserFavoritesResponseDTO dto = new UserFavoritesResponseDTO();
      dto.setClassified(favorite.getClassified());
      dto.setUser(favorite.getUser());
      dto.setCreatedAt(favorite.getCreatedAt());
      dto.setUpdatedAt(favorite.getUpdatedAt());
      response.add(dto);
    });
    return response;
  }
}
