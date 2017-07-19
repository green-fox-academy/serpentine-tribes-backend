package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

  private final int newBuildingPrice = 250;
  private final int buildingLevelPrice = 100;
  private final int buildingLevelMax = 20;
  private final BuildingRepository buildingRepository;
  private final DtoService dtoService;
  private final KingdomService kingdomService;
  private final TimeService timeService;
  private final ResourceService resourceService;

  @Autowired
  public BuildingService(
      BuildingRepository buildingRepository,
      DtoService dtoService,
      KingdomService kingdomService,
      TimeService timeService,
      ResourceService resourceService) {
    this.buildingRepository = buildingRepository;
    this.dtoService = dtoService;
    this.kingdomService = kingdomService;
    this.timeService = timeService;
    this.resourceService = resourceService;
  }

  public BuildingListDto getBuildingList(long userId) {
    List<Building> buildings = buildingRepository.findAllByKingdomUserId(userId);
    List<Building> buildingsWithFinishedAtTime = timeService
        .setBuildingListFinishedTimes(buildings);
    return dtoService.convertToBuildingListDtoFromBuildings(buildingsWithFinishedAtTime);
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
    Building savedBuildingWithFinishedAtTime = saveNewBuilding(userId, newBuilding);
    BuildingDto buildingDto = dtoService.convertfromBuilding(savedBuildingWithFinishedAtTime);
    return buildingDto;
  }

  public Building createNewBuilding(String type) {
    return Building.builder()
        .type(type)
        .startedAt(new Timestamp(System.currentTimeMillis()))
        .build();
  }

  public Building saveNewBuilding(Long userId, Building building) {
    Kingdom kingdom = kingdomService.getKingdomOfUser(userId);
    kingdom.addBuilding(building);
    Building savedBuildingWithFinishedAtTime = saveBuilding(building);
    resourceService.decreaseResource(kingdom, ResourceType.GOLD, newBuildingPrice);
    return savedBuildingWithFinishedAtTime;
  }

  public BuildingDto updateBuilding(Long buildingId, Integer level) {
    Building building = buildingRepository.findOne(buildingId);
    building.setLevel(level);
    Building savedBuildingWithFinishedAtTime = saveBuilding(building);
    resourceService.decreaseResource(building.getKingdom(), ResourceType.GOLD, level * buildingLevelPrice);
    return dtoService.convertfromBuilding(savedBuildingWithFinishedAtTime);
  }

  public boolean existsByBuildingIdAndUserId(Long buildingId, Long userId) {
    return buildingRepository.existsByIdAndKingdomUserId(buildingId, userId);
  }

  public BuildingDto getBuildingData(Long buildingId) {
    Building building = buildingRepository.findOne(buildingId);
    Building buildingWithFinishedAtTime = timeService.setBuildingFinishedTime(building);
    return dtoService.convertfromBuilding(buildingWithFinishedAtTime);
  }

  public Building saveBuilding(Building building) {
    Building savedBuilding = buildingRepository.save(building);
    return timeService.setBuildingFinishedTime(savedBuilding);
  }

  public boolean hasEnoughGoldForNewBuilding(Long userId) {
    return checkGoldAmount(userId, newBuildingPrice);
  }

  public boolean hasEnoughGoldForUpgrade(Long userId, int level) {
    int price = level * buildingLevelPrice;
    return checkGoldAmount(userId, price);
  }

  public boolean checkGoldAmount(Long userId, int price) {
    Kingdom kingdom = kingdomService.getKingdomOfUser(userId);
    return resourceService.hasEnoughResource(kingdom, ResourceType.GOLD, price);
  }

  public boolean isUpgradeLevelAllowed(Long buildingId, int levelToReach) {
    Building buildingToUpgrade = buildingRepository.findOne(buildingId);
    BuildingType buildingType = buildingToUpgrade.getType();

    if (buildingType.equals(BuildingType.TOWNHALL) && levelToReach <= buildingLevelMax) {
      return true;
    }
    int levelOfTownhall = buildingRepository
        .findByTypeAndKingdomId(BuildingType.TOWNHALL, buildingToUpgrade.getKingdom().getId())
        .getLevel();
    if(levelToReach <= levelOfTownhall) {
      return true;
    }
    return false;
  }
}
