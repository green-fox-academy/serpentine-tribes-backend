package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.repository.ResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  private final KingdomService kingdomService;
  private final ResourceRepository resourceRepository;
  private final BuildingService buildingService;

  @Autowired
  public ResourceService (KingdomService kingdomService,
      ResourceRepository resourceRepository, BuildingService buildingService) {
    this.kingdomService = kingdomService;
    this.resourceRepository = resourceRepository;
    this.buildingService = buildingService;
  }

  public void increaseResourceByGenerationForKingdom(Kingdom kingdom){
    Resource foodToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    Resource goldToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId());

    if (isEnoughSpaceInFoodStorage(kingdom)) {
      foodToChange.setAmount(foodToChange.getAmount() + foodToChange.getGeneration());
    }

    if (isEnoughSpaceInGoldStorage(kingdom)) {
      goldToChange.setAmount(goldToChange.getAmount() + goldToChange.getGeneration());
    }
      kingdomService.saveKingdom(kingdom);
    }

  public void calculateGenerationAmountForKingdom(Kingdom kingdom) {
    List<Building> buildingsOfKingdom = kingdom.getBuildings();
    int setGoldGenerationTo = 0;
    int setFoodGenerationTo = 0;
    for (Building building : buildingsOfKingdom) {
      if (building.getType().equals(BuildingType.MINE)){
        setGoldGenerationTo += building.getFinishedLevel()*10;
      } else if (building.getType().equals(BuildingType.FARM)) {
        setFoodGenerationTo += building.getFinishedLevel()*10;
      } else if (building.getType().equals(BuildingType.TOWNHALL)){
        setGoldGenerationTo += building.getFinishedLevel()*10;
        setFoodGenerationTo += building.getFinishedLevel()*10;
      }
    }
    setResourceGeneration(kingdom, setGoldGenerationTo, setFoodGenerationTo);
  }

  public void setResourceGeneration(Kingdom kingdom, int goldGeneration, int foodGeneration){
    Resource foodToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    Resource goldToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId());
    goldToChange.setGeneration(goldGeneration);
    foodToChange.setGeneration(foodGeneration);
    kingdomService.saveKingdom(kingdom);
  }

  public boolean isEnoughSpaceInGoldStorage (Kingdom kingdom) {
    int townhallLevel = buildingService.findBuildingByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
    int goldInKingdom = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId()).getAmount();
    if (goldInKingdom < townhallLevel*1000) {
      return true;
    }
    return false;
  }

  public boolean isEnoughSpaceInFoodStorage (Kingdom kingdom) {
    int townhallLevel = buildingService.findBuildingByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
    int foodInKingdom = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId()).getAmount();
    if (foodInKingdom < townhallLevel*1000) {
      return true;
    }
    return false;
  }
}
