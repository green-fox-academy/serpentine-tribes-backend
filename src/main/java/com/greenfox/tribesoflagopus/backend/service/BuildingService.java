package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  @Autowired
  KingdomRepository kingdomRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  DtoService dtoService;

  @Autowired
  ErrorService errorService;

  public ResponseEntity<JsonDto> createListOfBuildings(long userId) {

    if(!userIdExists(userId)) {
      StatusResponse userIdNotFoundStatus = errorService.getUserIdNotFoundStatus();
      return ResponseEntity.status(404).body(userIdNotFoundStatus);
    }

    BuildingListDto buildings = createBuildingList(userId);
    return ResponseEntity.ok().body(buildings);
  }

  private BuildingListDto createBuildingList(long userId) {
    List<Building> buildingsToConvertToDto = kingdomRepository.findOneByUserId(userId).getBuildings();
    List<BuildingDto> buildingDtos = dtoService.convertFromBuildings(buildingsToConvertToDto);
    BuildingListDto buildingsToReturn = BuildingListDto.builder()
        .buildings(buildingDtos)
        .build();
    return buildingsToReturn;
  }

  private boolean userIdExists(long userId) {
    return userRepository.exists(userId);
  }

}
