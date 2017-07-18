package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.TroopRepository;

import java.sql.Timestamp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;
  private final TroopRepository troopRepository;
  private final TimeService timeService;

  @Autowired
  public TroopService(DtoService dtoService,
      KingdomRepository kingdomRepository,
      TroopRepository troopRepository, TimeService timeService) {

    this.dtoService = dtoService;
    this.kingdomRepository = kingdomRepository;
    this.troopRepository = troopRepository;
    this.timeService = timeService;
  }

  public TroopListDto listTroopsOfUser(Long userId) {

    Kingdom foundKingdom = kingdomRepository.findOneByUserId(userId);
    List<Troop> troopsToConvertToDto = foundKingdom.getTroops();
    List<Troop> troopsWithFinishedAtTime = setFinishedAtTimesOfList(troopsToConvertToDto);
    TroopListDto troopsToReturn = dtoService.createTroopListDto(troopsWithFinishedAtTime);

    return troopsToReturn;
  }

  public boolean existsByIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public TroopDto fetchTroop(Long userId, Long troopId) {

    Troop foundTroop = troopRepository.findOneByIdAndKingdomUserId(troopId, userId);
    Troop foundTroopWithFinishedAtTime = setFinishedAtTime(foundTroop);

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
        .hp(1)
        .attack(1)
        .defence(1)
        .startedAt(new Timestamp(System.currentTimeMillis()))
        .build();
    Troop savedTroopWithFinishedAtTime = addTroopToUsersKingdom(newTroop, userId);
    return dtoService.convertFromTroop(savedTroopWithFinishedAtTime);
  }

  @Transactional
  public Troop addTroopToUsersKingdom(Troop troop, Long userId) {
    Kingdom existingKingdom = kingdomRepository.findOneByUserId(userId);
    existingKingdom.addTroop(troop);
    Troop savedTroop = saveTroop(troop);
    return savedTroop;
  }

  public TroopDto updateTroop(Long troopId, Integer level) {
    Troop troop = troopRepository.findById(troopId);
    troop.setLevel(level);
    Troop savedTroop = saveTroop(troop);
    return dtoService.convertFromTroop(savedTroop);
  }

  public boolean existsByTroopIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public List<Troop> setFinishedAtTimesOfList(List<Troop> troops) {
    for (Troop troop : troops) {
      troop = setFinishedAtTime(troop);
    }
    return troops;
  }

  public Troop setFinishedAtTime(Troop troop) {
    Timestamp finishedAt = timeService.calculateFinishedAtTime(troop.getStartedAt());
    troop.setFinishedAt(finishedAt);
    return troop;
  }

  public Troop saveTroop(Troop troop) {
    Troop savedTroop = troopRepository.save(troop);
    return setFinishedAtTime(savedTroop);
  }


}
