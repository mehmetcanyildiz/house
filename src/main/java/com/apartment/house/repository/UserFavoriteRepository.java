package com.apartment.house.repository;

import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserFavoriteModel;
import com.apartment.house.model.UserModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavoriteModel, String> {

  List<UserFavoriteModel> findByUser(UserModel user);

  Optional<UserFavoriteModel> findByUserAndClassified(UserModel user, ClassifiedModel classified);

}
