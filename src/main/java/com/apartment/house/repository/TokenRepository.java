package com.apartment.house.repository;

import com.apartment.house.enums.TokenTypeEnum;
import com.apartment.house.model.TokenModel;
import com.apartment.house.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, String> {

  Optional<TokenModel> findByToken(String token);

  Optional<TokenModel> findFirstByTypeAndUserOrderByCreatedAtDesc(
      TokenTypeEnum type,
      UserModel user
  );
}
