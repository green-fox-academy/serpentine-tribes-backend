package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
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
    int buildingGenerationAmount;
    int kingdomGenerationAmount;

    for (Building building : buildingsOfKingdom) {
      if (building.getType().equals(BuildingType.MINE)){

      }
    }
  }
}
