package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  private final KingdomRepository kingdomRepository;

  @Autowired
  public ResourceService (KingdomRepository kingdomRepository) {
    this.kingdomRepository = kingdomRepository;
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

    for (Building building : buildingsOfKingdom) {
      if (building.getType().equals(BuildingType.MINE)){
        increaseGoldGeneration(kingdom, building);
      } else if (building.getType().equals(BuildingType.FARM)){
        increaseFoodGeneration(kingdom, building)
      } else {
        increaseAllResourcesGeneration(kingdom, building)
      }
    }
    kingdomRepository.save(kingdom);
  }

  public void increaseGoldGeneration(Kingdom kingdom, Building mine){
    List<Resource> resourcesPerKingdom = kingdom.getResources();
    for (Resource resource : resourcesPerKingdom) {
      if (resource.getType().equals(ResourceType.GOLD)) {
        int increasedMineGeneration = resource.getGeneration() + mine.getLevel()*10;
        resource.setGeneration(increasedMineGeneration);
      }
    }
  }

  public void increaseFoodGeneration(Kingdom kingdom, Building farm){
    List<Resource> resourcesPerKingdom = kingdom.getResources();
    for (Resource resource : resourcesPerKingdom) {
      if (resource.getType().equals(ResourceType.FOOD)) {
        int increasedMineGeneration = resource.getGeneration() + farm.getLevel()*10;
        resource.setGeneration(increasedMineGeneration);
      }
    }
  }

  public void increaseAllResourcesGeneration(Kingdom kingdom, Building farm){
    List<Resource> resourcesPerKingdom = kingdom.getResources();
    for (Resource resource : resourcesPerKingdom) {
      if (resource.getType().equals(ResourceType.FOOD)) {
        int increasedMineGeneration = resource.getGeneration() + farm.getLevel()*10;
        resource.setGeneration(increasedMineGeneration);
      }
    }
  }
}
