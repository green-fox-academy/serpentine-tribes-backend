package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.TroopRepository;

import java.sql.Timestamp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

  private final int newTroopPrice = 10;
  private final int troopUpgradePrice = 5;
  private final DtoService dtoService;
  private final KingdomService kingdomService;
  private final TroopRepository troopRepository;
  private final TimeService timeService;
  private final ResourceService resourceService;

  @Autowired
  public TroopService(DtoService dtoService,
      KingdomService kingdomService,
      TroopRepository troopRepository,
      TimeService timeService,
      ResourceService resourceService) {

    this.dtoService = dtoService;
    this.kingdomService = kingdomService;
    this.troopRepository = troopRepository;
    this.timeService = timeService;
    this.resourceService = resourceService;
  }

  public TroopListDto listTroopsOfUser(Long userId) {
    List<Troop> troopsToConvertToDto = troopRepository.findAllByKingdomUserId(userId);
    List<Troop> troopsWithFinishedAtTime = timeService
        .setTroopListFinishedTimes(troopsToConvertToDto);
    TroopListDto troopsToReturn = dtoService.createTroopListDto(troopsWithFinishedAtTime);
    return troopsToReturn;
}

  public boolean existsByIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public TroopDto fetchTroop(Long userId, Long troopId) {

    Troop foundTroop = troopRepository.findOneByIdAndKingdomUserId(troopId, userId);
    Troop foundTroopWithFinishedAtTime = timeService.setTroopFinishedTime(foundTroop);

    return dtoService.convertFromTroop(foundTroopWithFinishedAtTime);
  }

  public boolean existsByUserId(Long userId) {
    return troopRepository.existsByKingdomUserId(userId);
  }

  public boolean existsByUserName(String username) {
    return troopRepository.existsByKingdomUserUsername(username);
  }

  public TroopDto addNewTroop(Long userId) {
    Troop newTroop = Troop.builder()
        .hp(10)
        .attack(1)
        .defence(1)
        .startedAt(new Timestamp(System.currentTimeMillis()))
        .build();
    Troop savedTroopWithFinishedAtTime = addTroopToUsersKingdom(newTroop, userId);
    pay(savedTroopWithFinishedAtTime.getKingdom(), newTroopPrice);
    return dtoService.convertFromTroop(savedTroopWithFinishedAtTime);
  }

  @Transactional
  public Troop addTroopToUsersKingdom(Troop troop, Long userId) {
    Kingdom existingKingdom = kingdomService.getKingdomOfUser(userId);
    existingKingdom.addTroop(troop);
    Troop savedTroop = saveTroop(troop);
    return savedTroop;
  }

  public TroopDto updateTroop(Long troopId, Integer level) {
    Troop troop = troopRepository.findOne(troopId);
    troop.setLevel(level);
    troop.setAttack(level);
    troop.setDefence(level);
    Troop savedTroop = saveTroop(troop);
    pay(troop.getKingdom(), troopUpgradePrice);
    return dtoService.convertFromTroop(savedTroop);
  }

  public boolean existsByTroopIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public Troop saveTroop(Troop troop) {
    Troop savedTroop = troopRepository.save(troop);
    return timeService.setTroopFinishedTime(savedTroop);
  }

  public boolean hasEnoughGoldForNewTroop(Long userId) {
    return checkGoldAmount(userId, newTroopPrice);
  }

  public boolean hasEnoughGoldForTroopUpgrade(Long userId) {
    return checkGoldAmount(userId, troopUpgradePrice);
  }

  public boolean checkGoldAmount(Long userId, int price) {
    Kingdom kingdom = kingdomService.getKingdomOfUser(userId);
    return resourceService.hasEnoughResource(kingdom, ResourceType.GOLD, price);
  }

  public void pay(Kingdom kingdom, int price) {
    resourceService.decreaseResource(kingdom, ResourceType.GOLD, price);
  }
}
