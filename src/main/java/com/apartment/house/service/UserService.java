package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.dto.user.UserFavoriteResponseDTO;
import com.apartment.house.dto.user.UserFavoritesResponseDTO;
import com.apartment.house.dto.user.UserUpdateRequestDTO;
import com.apartment.house.dto.user.UserUpdateResponseDTO;
import com.apartment.house.enums.EmailTemplateNameEnum;
import com.apartment.house.enums.RoleEnum;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserFavoriteModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.UserFavoriteRepository;
import com.apartment.house.repository.UserRepository;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserFavoriteRepository userFavoriteRepository;
  private final EmailService emailService;
  private final Environment environment;
  private final ApplicationConfig applicationConfig;

  public void validateUserByEmail(String email) throws Exception {
    Optional<UserModel> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new Exception("User not found");
    }
  }

  public UserModel findUserById(String id) throws UsernameNotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public void validateRegister(RegisterRequestDTO registerDTO) throws Exception {
    if (!registerDTO.getPassword().equals(registerDTO.getRe_password())) {
      throw new Exception("Passwords do not match");
    }
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new Exception("User already exists");
    }
  }

  private UserModel covertUserModel(RegisterRequestDTO registerDTO) {
    UserModel user = new UserModel();
    user.setEmail(registerDTO.getEmail());
    user.setFirstName(registerDTO.getFirstname());
    user.setLastName(registerDTO.getLastname());
    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
    user.setPhone(registerDTO.getPhone());
    user.setRole(RoleEnum.USER);

    return user;
  }

  public UserModel register(RegisterRequestDTO registerDTO) {
    UserModel user = covertUserModel(registerDTO);
    return userRepository.save(user);
  }

  public void save(UserModel user) {
    userRepository.save(user);
  }

  public UserModel findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public UserUpdateResponseDTO update(UserModel user, UserUpdateRequestDTO requestDTO) {
    user.setFirstName(requestDTO.getFirstname());
    user.setLastName(requestDTO.getLastname());
    user.setPhone(requestDTO.getPhone());
    userRepository.save(user);

    UserUpdateResponseDTO response = new UserUpdateResponseDTO();
    response.setStatus(true);
    response.setMessage("User updated");
    response.setId(user.getId());

    return response;
  }

  public UserFavoriteResponseDTO addFavorite(UserModel user, ClassifiedModel classified)
      throws MessagingException {
    UserFavoriteModel model = new UserFavoriteModel();
    model.setUser(user);
    model.setClassified(classified);
    userFavoriteRepository.save(model);

    String url = applicationConfig.baseWebUrl + "/classfied/detail/" + classified.getSlug();

    emailService.sendEmail(
        classified.getUser().getEmail(),
        classified.getUser().getFirstName(),
        EmailTemplateNameEnum.CLASSIFIED_FAVORITE,
        url,
        classified.getTitle(),
        "Favorite added"
    );

    UserFavoriteResponseDTO response = new UserFavoriteResponseDTO();
    response.setStatus(true);
    response.setMessage("Favorite added");
    response.setId(user.getId() + " => " + classified.getId());

    return response;
  }

  public UserFavoriteResponseDTO deleteFavorite(UserModel user,
      ClassifiedModel classified) {

    UserFavoriteModel model = userFavoriteRepository.findByUserAndClassified(user, classified)
        .orElseThrow(() -> new RuntimeException("Favorite not found"));

    userFavoriteRepository.delete(model);

    UserFavoriteResponseDTO response = new UserFavoriteResponseDTO();
    response.setStatus(true);
    response.setMessage("Favorite deleted");
    response.setId(classified.getId());

    return response;
  }

  public List<UserFavoritesResponseDTO> getFavorites(UserModel user) {
    List<UserFavoriteModel> favorites = userFavoriteRepository.findByUser(user);
    if (favorites.isEmpty()) {
      throw new RuntimeException("Favorites not found");
    }

    return UserFavoritesResponseDTO.convert(favorites);
  }

  public Boolean isFavorite(UserModel authUser, ClassifiedModel classifiedModel) {
    return userFavoriteRepository.findByUserAndClassified(authUser, classifiedModel).isPresent();
  }
}
