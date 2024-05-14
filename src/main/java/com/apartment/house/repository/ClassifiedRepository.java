package com.apartment.house.repository;

import com.apartment.house.enums.StatusEnum;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiedRepository extends JpaRepository<ClassifiedModel, String> {

  List<ClassifiedModel> findByUser(UserModel user);

  List<ClassifiedModel> findByUserAndStatus(UserModel user, StatusEnum statusEnum);

  List<ClassifiedModel> findByStatus(StatusEnum statusEnum);

  Optional<ClassifiedModel> findByIdAndStatus(String id, StatusEnum statusEnum);
}
