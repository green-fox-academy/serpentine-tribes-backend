package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BackgroundTaskService {

  private final KingdomService kingdomService;
  private final ResourceService resourceService;

  @Autowired
  BackgroundTaskService(KingdomService kingdomService, ResourceService resourceService){
    this.kingdomService = kingdomService;
    this.resourceService = resourceService;
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void increaseResourcesAmountPerMinuteInAllKingdoms() {
    List<Kingdom> allKingdoms = kingdomService.findAllKingdoms();

    for (Kingdom kingdom : allKingdoms) {
      resourceService.calculateGenerationAmountForKingdom(kingdom);
      resourceService.increaseResourceByGenerationForKingdom(kingdom);
    }
  }
}
