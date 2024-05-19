package com.apartment.house.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.dto.classified.*;
import com.apartment.house.enums.ClassifiedCategoryEnum;
import com.apartment.house.enums.ClassifiedStatusEnum;
import com.apartment.house.enums.ClassifiedTypeEnum;
import com.apartment.house.enums.EmailTemplateNameEnum;
import com.apartment.house.enums.StatusEnum;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.model.UserModel;
import com.apartment.house.repository.ClassifiedRepository;
import com.apartment.house.util.SlugUtil;
import jakarta.mail.MessagingException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class ClassifiedServiceTest {

  @Mock
  private ClassifiedRepository classifiedRepository;

  @Mock
  private ClassifiedImageService classifiedImageService;

  @Mock
  private AuthService authService;

  @Mock
  private UserService userService;

  @Mock
  private EmailService emailService;

  @Mock
  private ApplicationConfig applicationConfig;

  @InjectMocks
  private ClassifiedService classifiedService;

  private CreateRequestDTO createRequestDTO;
  private ClassifiedModel classifiedModel;
  private UserModel userModel;
  private CreateImageResponseDTO createImageResponseDTO;

  @BeforeEach
  void setUp() {
    createRequestDTO = new CreateRequestDTO();
    createRequestDTO.setTitle("Test Title");
    createRequestDTO.setDescription("Test Description");
    createRequestDTO.setPrice(100000.0);
    createRequestDTO.setType(ClassifiedTypeEnum.SALE);
    createRequestDTO.setCategory(ClassifiedCategoryEnum.HOUSE);
    createRequestDTO.setRoomNumber(3);
    createRequestDTO.setLivingRoomNumber(1);
    createRequestDTO.setGrossArea(120);
    createRequestDTO.setNetArea(100);
    createRequestDTO.setBuildingAge(5);
    createRequestDTO.setFloorLocation(2);
    createRequestDTO.setTotalFloor(5);
    createRequestDTO.setIsFurnished(true);
    createRequestDTO.setImages(new ArrayList<>());

    userModel = new UserModel();
    userModel.setId(UUID.randomUUID().toString());
    userModel.setFirstName("John");
    userModel.setLastName("Doe");
    userModel.setEmail("john.doe@example.com");

    classifiedModel = new ClassifiedModel();
    classifiedModel.setId(UUID.randomUUID().toString());
    classifiedModel.setUser(userModel);
    classifiedModel.setTitle(createRequestDTO.getTitle());
    classifiedModel.setSlug(
        SlugUtil.toSlug(createRequestDTO.getTitle()) + "-" + System.currentTimeMillis());
    classifiedModel.setDescription(createRequestDTO.getDescription());
    classifiedModel.setPrice(createRequestDTO.getPrice());
    classifiedModel.setType(createRequestDTO.getType());
    classifiedModel.setCategory(createRequestDTO.getCategory());
    classifiedModel.setRoomNumber(createRequestDTO.getRoomNumber());
    classifiedModel.setLivingRoomNumber(createRequestDTO.getLivingRoomNumber());
    classifiedModel.setGrossArea(createRequestDTO.getGrossArea());
    classifiedModel.setNetArea(createRequestDTO.getNetArea());
    classifiedModel.setBuildingAge(createRequestDTO.getBuildingAge());
    classifiedModel.setFloorLocation(createRequestDTO.getFloorLocation());
    classifiedModel.setTotalFloor(createRequestDTO.getTotalFloor());
    classifiedModel.setIsFurnished(createRequestDTO.getIsFurnished());
    classifiedModel.setClassifiedStatus(ClassifiedStatusEnum.ACTIVE);
    classifiedModel.setStatus(StatusEnum.ACTIVE);

    createImageResponseDTO = new CreateImageResponseDTO();
    createImageResponseDTO.setStatus(true);
  }

  @Test
  void createClassifiedSuccess() throws MessagingException {
    when(authService.getAuthUser()).thenReturn(userModel);
    when(classifiedRepository.save(any(ClassifiedModel.class))).thenReturn(classifiedModel);
    when(classifiedImageService.uploadImages(any(CreateImageRequestDTO.class))).thenReturn(
        createImageResponseDTO);

    CreateResponseDTO responseDTO = classifiedService.create(createRequestDTO);

    verify(classifiedRepository, times(1)).save(any(ClassifiedModel.class));
    verify(classifiedImageService, times(1)).uploadImages(any(CreateImageRequestDTO.class));
    verify(emailService, times(1)).sendEmail(eq(userModel.getEmail()),
                                             eq(userModel.getFirstName() + " "
                                                    + userModel.getLastName()),
                                             eq(EmailTemplateNameEnum.CLASSIFIED_CREATED),
                                             anyString(), eq(classifiedModel.getTitle()),
                                             eq("Classified created")
    );

    assert responseDTO.isStatus();
    assert responseDTO.getMessage().equals("Classified created successfully");
  }

  @Test
  void createClassifiedImageUploadFailed() throws MessagingException {
    createImageResponseDTO.setStatus(false);
    when(authService.getAuthUser()).thenReturn(userModel);
    when(classifiedRepository.save(any(ClassifiedModel.class))).thenReturn(classifiedModel);
    when(classifiedImageService.uploadImages(any(CreateImageRequestDTO.class))).thenReturn(
        createImageResponseDTO);

    CreateResponseDTO responseDTO = classifiedService.create(createRequestDTO);

    verify(classifiedRepository, times(1)).save(any(ClassifiedModel.class));
    verify(classifiedRepository, times(1)).delete(any(ClassifiedModel.class));
    verify(classifiedImageService, times(1)).uploadImages(any(CreateImageRequestDTO.class));
    verify(emailService, times(0)).sendEmail(
        anyString(), anyString(), any(), anyString(), anyString(), anyString());

    assert !responseDTO.isStatus();
    assert responseDTO.getMessage().equals("Classified creation failed");
  }
}
