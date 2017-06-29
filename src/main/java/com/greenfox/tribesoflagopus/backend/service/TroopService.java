package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.TroopRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;
  private final TroopRepository troopRepository;

  @Autowired
  public TroopService(DtoService dtoService,
      KingdomRepository kingdomRepository,
      TroopRepository troopRepository) {

    this.dtoService = dtoService;
    this.kingdomRepository = kingdomRepository;
    this.troopRepository = troopRepository;
  }

  public TroopListDto listTroopsOfUser(Long userId) {

    Kingdom foundKingdom = kingdomRepository.findOneByUserId(userId);
    List<Troop> troopsToConvertToDto = foundKingdom.getTroops();
    TroopListDto troopsToReturn = dtoService.createTroopListDto(troopsToConvertToDto);

    return troopsToReturn;
  }

  public boolean existsByIdAndUserId(Long troopId, Long userId) {
    return troopRepository.existsByIdAndKingdomUserId(troopId, userId);
  }

  public TroopDto fetchTroop(Long userId, Long troopId) {

    Troop foundTroop = troopRepository.findOneByIdAndKingdomUserId(userId, troopId);

    return dtoService.convertFromTroop(foundTroop);
  }

  public boolean existsByUserId(Long userId) {
    return troopRepository.existsByKingdomUserId(userId);
  }

  public boolean existsByUserName(String username) {
    return troopRepository.existsByKingdomUserUsername(username);
  }

  public TroopDto addNewTroop(Long userId) {
    Troop newTroop = Troop.builder().build();
    addTroopToUsersKingdom(newTroop, userId);
    return dtoService.convertFromTroop(newTroop);
  }

  @Transactional
  public Troop addTroopToUsersKingdom(Troop troop, Long userId) {
    Kingdom existingKingdom = kingdomRepository.findOneByUserId(userId);
    existingKingdom.addTroop(troop);
    kingdomRepository.save(existingKingdom);
    return troop;
  }
}
