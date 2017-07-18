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

  private final KingdomRepository kingdomRepository;
  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceService (KingdomRepository kingdomRepository,
      ResourceRepository resourceRepository) {
    this.kingdomRepository = kingdomRepository;
    this.resourceRepository = resourceRepository;
  }

  public void increaseResourceByGenerationForKingdom(Kingdom kingdom){
      List<Resource> resourcesPerKingdom = kingdom.getResources();
      for (Resource resource : resourcesPerKingdom) {
        resource.setAmount(resource.getAmount() + resource.getGeneration());
      }
      kingdomRepository.save(kingdom);
    }

  public void calculateGenerationAmountForKingdom(Kingdom kingdom) {
    List<Building> buildingsOfKingdom = kingdom.getBuildings();
    int setGoldGenerationTo = 0;
    int setFoodGenerationTo = 0;
    for (Building building : buildingsOfKingdom) {
      if (building.getType().equals(BuildingType.MINE)){
        setGoldGenerationTo += building.getLevel()*10;
      } else if (building.getType().equals(BuildingType.FARM)) {
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

  public void setResourceGeneration(Kingdom kingdom, int goldGeneration, int foodGeneration){
    Resource foodToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.FOOD, kingdom.getId());
    Resource goldToChange = resourceRepository.findByTypeAndKingdomId(ResourceType.GOLD, kingdom.getId());
    goldToChange.setGeneration(goldGeneration);
    foodToChange.setGeneration(foodGeneration);
    kingdomRepository.save(kingdom);
  }
}
