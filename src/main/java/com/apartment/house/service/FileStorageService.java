package com.apartment.house.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  private final Path fileStorageLocation;

  public FileStorageService() {
    this.fileStorageLocation = Paths.get("uploads")
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public String storeFile(MultipartFile file, String newFileName) {
    try {
      Path targetLocation = this.fileStorageLocation.resolve(newFileName);

      Files.copy(file.getInputStream(), targetLocation);

      return "/uploads/" + newFileName;
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file " + newFileName + ". Please try again!", ex);
    }
  }

}