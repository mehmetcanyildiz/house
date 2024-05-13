package com.apartment.house.service;

import com.apartment.house.dto.classified.CreateRequestDTO;
import com.apartment.house.dto.classified.CreateResponseDTO;
import com.apartment.house.enums.ClassifiedStatusEnum;
import com.apartment.house.model.ClassifiedModel;
import com.apartment.house.repository.ClassifiedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassifiedService {

  private final ClassifiedRepository classifiedRepository;
  private final AuthService authService;

  public CreateResponseDTO create(CreateRequestDTO requestDTO) {
    ClassifiedModel classifiedModel = convertClassifiedModel(requestDTO);
    classifiedRepository.save(classifiedModel);

    CreateResponseDTO responseDTO = new CreateResponseDTO();
    responseDTO.setId(classifiedModel.getId());
    responseDTO.setStatus(true);
    responseDTO.setMessage("Classified created successfully");

    return responseDTO;
  }

  private ClassifiedModel convertClassifiedModel(CreateRequestDTO requestDTO) {
    ClassifiedModel classifiedModel = new ClassifiedModel();

    classifiedModel.setUser(authService.getAuthUser());
    classifiedModel.setTitle(requestDTO.getTitle());
    classifiedModel.setDescription(requestDTO.getDescription());
    classifiedModel.setPrice(requestDTO.getPrice());
    classifiedModel.setType(requestDTO.getType());
    classifiedModel.setCategory(requestDTO.getCategory());
    classifiedModel.setRoomNumber(requestDTO.getRoomNumber());
    classifiedModel.setGrossArea(requestDTO.getGrossArea());
    classifiedModel.setNetArea(requestDTO.getNetArea());
    classifiedModel.setBuildingAge(requestDTO.getBuildingAge());
    classifiedModel.setFloorLocation(requestDTO.getFloorLocation());
    classifiedModel.setTotalFloor(requestDTO.getTotalFloor());
    classifiedModel.setIsFurnished(requestDTO.getIsFurnished());
    classifiedModel.setClassifiedStatus(ClassifiedStatusEnum.ACTIVE);

    return classifiedModel;
  }
}
