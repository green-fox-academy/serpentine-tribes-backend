package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BackgroundTaskService {

  private final KingdomRepository kingdomRepository;

  @Autowired
  BackgroundTaskService(KingdomRepository kingdomRepository){
    this.kingdomRepository = kingdomRepository;
  }

  @Scheduled(fixedDelay = 60000)
  public void increaseResourcesAmountPerMinute() {
    List<Kingdom> allKingdoms = kingdomRepository.findAll();

    for (Kingdom kingdom : allKingdoms) {
      List<Resource> resourcesPerKingdom = kingdom.getResources();
      for (Resource resource : resourcesPerKingdom) {
        resource.setAmount(resource.getGeneration());
      }
    }
  }
}
