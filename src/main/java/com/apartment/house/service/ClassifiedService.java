package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.dto.classified.ClassifiedDTO;
import com.apartment.house.dto.classified.ClassifiedRequestDTO;
import com.apartment.house.dto.classified.ClassifiedUserDTO;
import com.apartment.house.dto.classified.CreateImageRequestDTO;
import com.apartment.house.dto.classified.CreateImageResponseDTO;
import com.apartment.house.dto.classified.CreateRequestDTO;
import com.apartment.house.dto.classified.CreateResponseDTO;
import com.apartment.house.dto.classified.DeleteResponseDTO;
import com.apartment.house.dto.classified.UpdateRequestDTO;
import com.apartment.house.dto.classified.UpdateResponseDTO;
import com.apartment.house.enums.ClassifiedStatusEnum;
import com.apartment.house.enums.EmailTemplateNameEnum;
import com.apartment.house.enums.StatusEnum;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.ClassifiedRepository;
import com.apartment.house.util.SlugUtil;
import jakarta.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassifiedService {

  private final ClassifiedRepository classifiedRepository;
  private final ClassifiedImageService classifiedImageService;
  private final AuthService authService;
  private final UserService userService;
  private final EmailService emailService;
  private final ApplicationConfig applicationConfig;

  public CreateResponseDTO create(CreateRequestDTO requestDTO) throws MessagingException {
    ClassifiedModel classifiedModel = convertClassifiedModel(requestDTO, null);
    classifiedRepository.save(classifiedModel);

    CreateImageRequestDTO imageRequest = new CreateImageRequestDTO();
    imageRequest.setImages(requestDTO.getImages());
    imageRequest.setClassified(classifiedModel);
    CreateImageResponseDTO imageResponse = classifiedImageService.uploadImages(imageRequest);

    if (!imageResponse.isStatus()) {
      classifiedRepository.delete(classifiedModel);
      CreateResponseDTO responseDTO = new CreateResponseDTO();
      responseDTO.setStatus(false);
      responseDTO.setMessage("Classified creation failed");
      return responseDTO;
    }

    emailService.sendEmail(classifiedModel.getUser().getEmail(),
                           classifiedModel.getUser().getFirstName() + " "
                               + classifiedModel.getUser().getLastName(),
                           EmailTemplateNameEnum.CLASSIFIED_CREATED,
                           applicationConfig.baseWebUrl + "/classified/detail/"
                               + classifiedModel.getSlug(), classifiedModel.getTitle(),
                           "Classified created"
    );

    CreateResponseDTO responseDTO = new CreateResponseDTO();
    responseDTO.setStatus(true);
    responseDTO.setId(classifiedModel.getId());
    responseDTO.setSlug(classifiedModel.getSlug());
    responseDTO.setMessage("Classified created successfully");

    return responseDTO;
  }

  private ClassifiedModel convertClassifiedModel(ClassifiedRequestDTO requestDTO,
      ClassifiedModel classifiedModel) {
    if (classifiedModel == null) {
      classifiedModel = new ClassifiedModel();
    }

    classifiedModel.setUser(authService.getAuthUser());
    classifiedModel.setTitle(requestDTO.getTitle());
    classifiedModel.setSlug(
        SlugUtil.toSlug(requestDTO.getTitle()) + "-" + System.currentTimeMillis());
    classifiedModel.setDescription(requestDTO.getDescription());
    classifiedModel.setPrice(requestDTO.getPrice());
    classifiedModel.setType(requestDTO.getType());
    classifiedModel.setCategory(requestDTO.getCategory());
    classifiedModel.setRoomNumber(requestDTO.getRoomNumber());
    classifiedModel.setLivingRoomNumber(requestDTO.getLivingRoomNumber());
    classifiedModel.setGrossArea(requestDTO.getGrossArea());
    classifiedModel.setNetArea(requestDTO.getNetArea());
    classifiedModel.setBuildingAge(requestDTO.getBuildingAge());
    classifiedModel.setFloorLocation(requestDTO.getFloorLocation());
    classifiedModel.setTotalFloor(requestDTO.getTotalFloor());
    classifiedModel.setIsFurnished(requestDTO.getIsFurnished());
    classifiedModel.setClassifiedStatus(ClassifiedStatusEnum.ACTIVE);
    classifiedModel.setStatus(StatusEnum.ACTIVE);

    return classifiedModel;
  }

  public ClassifiedDTO getClassifiedBySlug(String slug) {
    ClassifiedModel classifiedModel = classifiedRepository.findBySlugAndStatus(slug, StatusEnum.ACTIVE)
        .orElseThrow(() -> new RuntimeException("Classified not found"));

    return convertClassifiedDTO(classifiedModel);
  }

  private ClassifiedDTO convertClassifiedDTO(ClassifiedModel classifiedModel) {
    ClassifiedDTO classifiedDTO = new ClassifiedDTO();
    ClassifiedUserDTO classifiedUserDTO = new ClassifiedUserDTO();

    classifiedDTO.setId(classifiedModel.getId());
    classifiedDTO.setTitle(classifiedModel.getTitle());
    classifiedDTO.setSlug(classifiedModel.getSlug());
    classifiedDTO.setDescription(classifiedModel.getDescription());
    classifiedDTO.setPrice(classifiedModel.getPrice());
    classifiedDTO.setType(classifiedModel.getType());
    classifiedDTO.setCategory(classifiedModel.getCategory());
    classifiedDTO.setRoomNumber(classifiedModel.getRoomNumber());
    classifiedDTO.setLivingRoomNumber(classifiedModel.getLivingRoomNumber());
    classifiedDTO.setGrossArea(classifiedModel.getGrossArea());
    classifiedDTO.setNetArea(classifiedModel.getNetArea());
    classifiedDTO.setBuildingAge(classifiedModel.getBuildingAge());
    classifiedDTO.setFloorLocation(classifiedModel.getFloorLocation());
    classifiedDTO.setTotalFloor(classifiedModel.getTotalFloor());
    classifiedDTO.setIsFurnished(classifiedModel.getIsFurnished());
    classifiedDTO.setClassifiedStatus(classifiedModel.getClassifiedStatus());
    classifiedDTO.setImages(classifiedImageService.getClassifiedImages(classifiedModel));

    classifiedDTO.setIsFavorite(userService.isFavorite(authService.getAuthUser(), classifiedModel));

    // User Details
    classifiedUserDTO.setFirstname(classifiedModel.getUser().getFirstName());
    classifiedUserDTO.setLastname(classifiedModel.getUser().getLastName());
    classifiedUserDTO.setEmail(classifiedModel.getUser().getEmail());
    classifiedUserDTO.setPhone(classifiedModel.getUser().getPhone());
    classifiedDTO.setUser(classifiedUserDTO);

    return classifiedDTO;
  }

  public List<ClassifiedDTO> getClassifiedByUserId(String id) {
    UserModel user = userService.findUserById(id);
    List<ClassifiedModel> classifiedModel = classifiedRepository.findByUserAndStatus(
        user, StatusEnum.ACTIVE);
    if (classifiedModel.isEmpty()) {
      throw new RuntimeException("Classified not found");
    }
    List<ClassifiedDTO> classifiedDTO = new ArrayList<>();
    classifiedModel.forEach(classified -> classifiedDTO.add(convertClassifiedDTO(classified)));

    return classifiedDTO;
  }

  public List<ClassifiedDTO> getItems() {
    List<ClassifiedModel> classifiedModel = classifiedRepository.findByStatus(StatusEnum.ACTIVE);
    if (classifiedModel.isEmpty()) {
      throw new RuntimeException("Classified not found");
    }
    List<ClassifiedDTO> classifiedDTO = new ArrayList<>();
    classifiedModel.forEach(classified -> classifiedDTO.add(convertClassifiedDTO(classified)));

    return classifiedDTO;
  }

  public DeleteResponseDTO delete(String id) {
    ClassifiedModel classifiedModel = classifiedRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Classified not found"));

    if (!classifiedModel.getUser().getId().equals(authService.getAuthUser().getId())) {
      throw new RuntimeException("Unauthorized");
    }

    if (classifiedModel.getStatus().equals(StatusEnum.DELETED)) {
      throw new RuntimeException("Classified already deleted");
    }

    classifiedModel.setStatus(StatusEnum.DELETED);
    classifiedRepository.save(classifiedModel);

    classifiedImageService.deleteImages(classifiedModel);

    DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO();
    deleteResponseDTO.setStatus(true);
    deleteResponseDTO.setId(classifiedModel.getId());
    deleteResponseDTO.setMessage("Classified and images deleted");

    return deleteResponseDTO;
  }

  public UpdateResponseDTO update(String id, UpdateRequestDTO requestDTO) {
    ClassifiedModel classifiedModel = classifiedRepository.findByIdAndStatus(id, StatusEnum.ACTIVE)
        .orElseThrow(() -> new RuntimeException("Classified not found"));

    if (!classifiedModel.getUser().getId().equals(authService.getAuthUser().getId())) {
      throw new RuntimeException("Unauthorized");
    }

    classifiedModel = convertClassifiedModel(requestDTO, classifiedModel);
    classifiedModel.setClassifiedStatus(
        ClassifiedStatusEnum.valueOf(requestDTO.getClassifiedStatus()));
    classifiedRepository.save(classifiedModel);

    CreateImageRequestDTO imageRequest = new CreateImageRequestDTO();
    imageRequest.setImages(requestDTO.getImages());
    imageRequest.setClassified(classifiedModel);
    CreateImageResponseDTO imageResponse = classifiedImageService.uploadImages(imageRequest);

    UpdateResponseDTO responseDTO = new UpdateResponseDTO();
    if (!imageResponse.isStatus()) {
      classifiedRepository.delete(classifiedModel);
      responseDTO.setStatus(false);
      responseDTO.setMessage("Classified update failed");

      return responseDTO;
    }

    responseDTO.setId(classifiedModel.getId());
    responseDTO.setStatus(true);
    responseDTO.setMessage("Classified updated successfully");

    return responseDTO;
  }

  public DeleteResponseDTO deleteImage(String id) {
    classifiedImageService.deleteImage(id);
    DeleteResponseDTO deleteResponseDTO = new DeleteResponseDTO();
    deleteResponseDTO.setStatus(true);
    deleteResponseDTO.setId(id);
    deleteResponseDTO.setMessage("Image deleted");

    return deleteResponseDTO;
  }

  public ClassifiedModel findClassifiedById(String classifiedId) {
    return classifiedRepository.findById(classifiedId)
        .orElseThrow(() -> new RuntimeException("Classified not found"));
  }
}
