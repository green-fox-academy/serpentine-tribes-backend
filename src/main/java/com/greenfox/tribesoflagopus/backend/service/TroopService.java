package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.TroopListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;

  @Autowired
  public TroopService(DtoService dtoService, KingdomRepository kingdomRepository) {
    this.dtoService = dtoService;
    this.kingdomRepository = kingdomRepository;
  }

  public TroopListDto listTroopsOfUser(Long userId) {

    List<Troop> troopsToConvertToDto = kingdomRepository.findOneByUserId(userId).getTroops();
    TroopListDto troopsToReturn = dtoService.createTroopListDto(troopsToConvertToDto);

    return troopsToReturn;
  }
}
