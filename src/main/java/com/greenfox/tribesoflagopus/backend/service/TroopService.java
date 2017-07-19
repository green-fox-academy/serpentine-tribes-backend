package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.TroopRepository;

import java.sql.Timestamp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

  private final DtoService dtoService;
  private final KingdomService kingdomService;
  private final TroopRepository troopRepository;

  @Autowired
  public TroopService(DtoService dtoService,
      KingdomService kingdomService,
      TroopRepository troopRepository) {

    this.dtoService = dtoService;
    this.kingdomService = kingdomService;
    this.troopRepository = troopRepository;
  }

  public TroopListDto listTroopsOfUser(Long userId) {
    List<Troop> troopsToConvertToDto = troopRepository.findAllByKingdomUserId(userId);
    TroopListDto troopsToReturn = dtoService.createTroopListDto(troopsToConvertToDto);
    return troopsToReturn;
}

  public boolean existsByIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public TroopDto fetchTroop(Long userId, Long troopId) {
    Troop foundTroop = troopRepository.findOneByIdAndKingdomUserId(troopId, userId);
    return dtoService.convertFromTroop(foundTroop);
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
    Kingdom existingKingdom = kingdomService.getKingdomOfUser(userId);
    existingKingdom.addTroop(troop);
    Troop savedTroop = saveTroop(troop);
    return savedTroop;
  }

  public TroopDto updateTroop(Long troopId, Integer level) {
    Troop troop = troopRepository.findOne(troopId);
    troop.setLevel(level);
    Troop savedTroop = saveTroop(troop);
    return dtoService.convertFromTroop(savedTroop);
  }

  public boolean existsByTroopIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public Troop saveTroop(Troop troop) {
    return troopRepository.save(troop);
  }
}
