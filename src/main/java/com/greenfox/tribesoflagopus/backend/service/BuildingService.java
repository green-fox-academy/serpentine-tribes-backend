package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  private final BuildingRepository buildingRepository;
  private final DtoService dtoService;
  private final KingdomService kingdomService;

  @Autowired
  public BuildingService(
      BuildingRepository buildingRepository,
      DtoService dtoService,
      KingdomService kingdomService) {
    this.buildingRepository = buildingRepository;
    this.dtoService = dtoService;
    this.kingdomService = kingdomService;
  }

  public BuildingListDto getBuildingList(long userId) {
    List<Building> buildings = buildingRepository.findAllByKingdomUserId(userId);
    return dtoService.convertToBuildingListDtoFromBuildings(buildings);
}

  public boolean validBuildingType(String inputBuildingType) {
    for (BuildingType buildingType : BuildingType.values()) {
      if (buildingType.toString().equals(inputBuildingType)) {
        return true;
      }
    }
    return false;
  }

  public BuildingDto addNewBuilding(String type, long userId) {
    Building newBuilding = createNewBuilding(type);
    Building savedBuilding = addNewBuilding(userId, newBuilding);
    return dtoService.convertfromBuilding(savedBuilding);
  }

  public Building createNewBuilding(String type) {
    return Building.builder()
        .type(type)
        .startedAt(new Timestamp(System.currentTimeMillis()))
        .build();
  }

  public Building addNewBuilding(Long userId, Building building) {
    Kingdom kingdom = kingdomService.getKingdomOfUser(userId);
    kingdom.addBuilding(building);
    return saveBuilding(building);
  }

  public BuildingDto updateBuilding(Long buildingId, Integer level) {
    Building building = buildingRepository.findOne(buildingId);
    building.setLevel(level);
    Building savedBuilding = saveBuilding(building);
    return dtoService.convertfromBuilding(savedBuilding);
  }

  public boolean existsByBuildingIdAndUserId(Long buildingId, Long userId) {
    return buildingRepository.existsByIdAndKingdomUserId(buildingId, userId);
  }

  public BuildingDto getBuildingData(Long buildingId) {
    Building building = buildingRepository.findOne(buildingId);
    return dtoService.convertfromBuilding(building);
  }

  public Building saveBuilding(Building building) {
    return buildingRepository.save(building);
  }

  public Building findBuildingByTypeAndKingdomId (BuildingType buildingType, long kingdomId) {
    return buildingRepository.findByTypeAndKingdomId(buildingType, kingdomId);
  }
}
