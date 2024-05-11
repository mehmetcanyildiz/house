package com.apartment.house.repository;

import com.apartment.house.model.entity.Token;
import com.apartment.house.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
}
