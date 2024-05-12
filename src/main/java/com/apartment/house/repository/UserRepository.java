package com.apartment.house.repository;

import com.apartment.house.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
