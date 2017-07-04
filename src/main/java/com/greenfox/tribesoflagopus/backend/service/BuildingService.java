package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingLevelInputDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  @Autowired
  BuildingRepository buildingRepository;

  @Autowired
  KingdomRepository kingdomRepository;

  @Autowired
  DtoService dtoService;

  public BuildingListDto getBuildingList(long userId) {
    List<Building> buildingsToConvertToDto = findKingdomByUserId(userId)
        .getBuildings();
    List<BuildingDto> buildingDtos = dtoService.convertFromBuildings(buildingsToConvertToDto);
    BuildingListDto buildingsToReturn = BuildingListDto.builder()
        .buildings(buildingDtos)
        .build();
    return buildingsToReturn;
  }

  public boolean validBuildingType(String inputBuildingType) {
    if (inputBuildingType.equals("farm")
        || inputBuildingType.equals("mine")
        || inputBuildingType.equals("barrack")) {
      return true;
    }
    return false;
  }

  public BuildingDto addNewBuilding(String type, long userId) {
    Kingdom kingdomOfNewBuilding = findKingdomByUserId(userId);
    createAndSaveNewBuilding(type, kingdomOfNewBuilding);
    BuildingDto buildingDto = getTheNewestBuildingByTypeAndUser(kingdomOfNewBuilding, type);
    return buildingDto;
  }

  public void createAndSaveNewBuilding(String inputBuildingType, Kingdom kingdom) {
    Building newBuilding = Building.builder()
        .type(inputBuildingType)
        .build();
    kingdom.addBuilding(newBuilding);
    kingdomRepository.save(kingdom);
  }

  public BuildingDto getTheNewestBuildingByTypeAndUser(Kingdom kingdom, String type) {
    Building building = buildingRepository.findTopByKingdomAndTypeOrderByIdDesc(kingdom, type);
    return dtoService.convertfromBuilding(building);
  }

  public BuildingDto updateBuilding(Long buildingId, BuildingLevelInputDto buildingLevelInputDto) {
    Building building = buildingRepository.findById(buildingId);
    building.setLevel(buildingLevelInputDto.getLevel());
    buildingRepository.save(building);
    return dtoService.convertfromBuilding(building);
  }

  public boolean existsBuildingById(Long buildingId) {
    return buildingRepository.exists(buildingId);
  }

  public Kingdom findKingdomByUserId(Long userId) {
    return kingdomRepository.findOneByUserId(userId);
  }


}


