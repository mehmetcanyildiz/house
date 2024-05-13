package com.apartment.house.service;

import com.apartment.house.dto.auth.RegisterRequestDTO;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

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
}
