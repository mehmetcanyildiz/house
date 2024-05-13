package com.apartment.house.repository;

import com.apartment.house.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

  Optional<UserModel> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);
}
