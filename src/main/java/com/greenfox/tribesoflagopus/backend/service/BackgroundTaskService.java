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

  private final KingdomRepository kingdomRepository;
  private final ResourceService resourceService;

  @Autowired
  BackgroundTaskService(KingdomRepository kingdomRepository, ResourceService resourceService){
    this.kingdomRepository = kingdomRepository;
    this.resourceService = resourceService;
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void increaseResourcesAmountPerMinuteInAllKingdoms() {
    List<Kingdom> allKingdoms = kingdomRepository.findAll();

    for (Kingdom kingdom : allKingdoms) {
      resourceService.calculateGenerationAmountForKingdom(kingdom);
      resourceService.increaseResourceByGenerationForKingdom(kingdom);
    }
  }
}
