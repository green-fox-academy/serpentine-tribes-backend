package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.repository.BuildingRepository;
import com.greenfox.tribesoflagopus.backend.repository.ResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  private final KingdomService kingdomService;
  private final ResourceRepository resourceRepository;
  private final BuildingRepository buildingRepository;

  @Autowired
  public ResourceService (KingdomService kingdomService,
      ResourceRepository resourceRepository, BuildingRepository buildingRepository) {
    this.kingdomService = kingdomService;
    this.resourceRepository = resourceRepository;
    this.buildingRepository = buildingRepository;
  }

  public void increaseResourceByGenerationForKingdom(Kingdom kingdom){
    Resource foodToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    Resource goldToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId());

    if (isEnoughSpaceInFoodStorage(kingdom)) {
      int townhallLevel = buildingRepository.findByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
      int amountToSet = foodToChange.getAmount() + foodToChange.getGeneration();
      int newAmount = (amountToSet > townhallLevel*1000) ? townhallLevel*1000 : amountToSet;
      foodToChange.setAmount(newAmount);
      resourceRepository.save(foodToChange);
    }

    if (isEnoughSpaceInGoldStorage(kingdom)) {
      int townhallLevel = buildingRepository.findByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
      int amountToSet = goldToChange.getAmount() + goldToChange.getGeneration();
      int newAmount = (amountToSet > townhallLevel*1000) ? townhallLevel*1000 : amountToSet;
      goldToChange.setAmount(newAmount);
      resourceRepository.save(goldToChange);
    }
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

  public void setResourceGeneration(Kingdom kingdom, int goldGeneration, int foodGeneration) {
    Resource foodToChange = resourceRepository
        .findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    Resource goldToChange = resourceRepository
        .findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId());
    goldToChange.setGeneration(goldGeneration);
    foodToChange.setGeneration(foodGeneration);
    kingdomService.saveKingdom(kingdom);
  }

  public boolean isEnoughSpaceInGoldStorage(Kingdom kingdom) {
    int townhallLevel = buildingRepository.findByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
    int goldAmountInKingdom = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId()).getAmount();
    if (goldAmountInKingdom < townhallLevel*1000) {
      return true;
    }
    return false;
  }

  public boolean isEnoughSpaceInFoodStorage(Kingdom kingdom) {
    int townhallLevel = buildingRepository.findByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getLevel();
    int foodAmountInKingdom = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId()).getAmount();
    if (foodAmountInKingdom < townhallLevel*1000) {
      return true;
    }
    return false;
  }

  public void feedTroops(Kingdom kingdom) {
    Resource foodInKingdom = resourceRepository
        .findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    int numberOfTroops = kingdom.getTroops().size();
    int currentFoodAmountInKingdom = foodInKingdom.getAmount();
    int foodAmountAfterFeeding = currentFoodAmountInKingdom - numberOfTroops;

    if (foodAmountAfterFeeding >= 0) {
      foodInKingdom.setAmount(foodAmountAfterFeeding);
    } else if (foodAmountAfterFeeding < 0) {
      foodInKingdom.setAmount(0);
    }
  }

  public boolean hasEnoughResource(Kingdom kingdom, ResourceType resourceType,
      int neededResourceAmount) {
    Resource resourceToCheck = resourceRepository.findByTypeAndKingdomId(resourceType, kingdom.getId());
      if (resourceToCheck.getAmount() >= neededResourceAmount) {
        return true;
      }
    return false;
  }

  public void decreaseResource(Kingdom kingdom, ResourceType resourceType, int decreaseAmount) {
    Resource resourceToDecrease = resourceRepository.findByTypeAndKingdomId(resourceType, kingdom.getId());
    int newAmount = resourceToDecrease.getAmount() - decreaseAmount;
    resourceToDecrease.setAmount(newAmount);
    resourceRepository.save(resourceToDecrease);
  }
}
