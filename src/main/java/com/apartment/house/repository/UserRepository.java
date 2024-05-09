package com.apartment.house.repository;

import com.apartment.house.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
