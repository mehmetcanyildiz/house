package com.apartment.house.repository;

import com.apartment.house.enums.StatusEnum;
import com.apartment.house.model.ClassifiedImageModel;
import com.apartment.house.model.ClassifiedModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiedImageRepository extends JpaRepository<ClassifiedImageModel, String> {

  List<ClassifiedImageModel> findByClassified(ClassifiedModel classifiedModel);

  List<ClassifiedImageModel> findByClassifiedAndStatus(ClassifiedModel classifiedModel,
      StatusEnum statusEnum);
}
