package com.apartment.house.repository;

import com.apartment.house.model.ClassifiedModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiedRepository extends JpaRepository<ClassifiedModel, String> {

}
