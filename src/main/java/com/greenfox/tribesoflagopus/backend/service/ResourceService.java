package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.ResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  private final KingdomService kingdomService;
  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceService (KingdomService kingdomService,
      ResourceRepository resourceRepository) {
    this.kingdomService = kingdomService;
    this.resourceRepository = resourceRepository;
  }

  public void increaseResourceByGenerationForKingdom(Kingdom kingdom) {
    List<Resource> resourcesPerKingdom = kingdom.getResources();
    for (Resource resource : resourcesPerKingdom) {
      resource.setAmount(resource.getAmount() + resource.getGeneration());
    }
    kingdomService.saveKingdom(kingdom);
  }

  public void calculateGenerationAmountForKingdom(Kingdom kingdom) {
    List<Building> buildingsOfKingdom = kingdom.getBuildings();
    int setGoldGenerationTo = 0;
    int setFoodGenerationTo = 0;
    for (Building building : buildingsOfKingdom) {
      if (building.isFinished() && building.getType().equals(BuildingType.MINE)){
        setGoldGenerationTo += building.getLevel()*10;
      } else if (building.isFinished() && building.getType().equals(BuildingType.FARM)) {
        setFoodGenerationTo += building.getLevel()*10;
      } else if (building.getType().equals(BuildingType.TOWNHALL)){
        setGoldGenerationTo += building.getLevel()*10;
        setFoodGenerationTo += building.getLevel()*10;
      } else {
        setFoodGenerationTo += 0;
        setGoldGenerationTo += 0;
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

  //Todo: finish this method
  public boolean hasEnoughResource(Long userId, ResourceType resourceType,
      int neededResourceAmount) {
    return true;
  }
}
