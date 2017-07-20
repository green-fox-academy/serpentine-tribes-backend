package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomInputModifyDto;
import com.greenfox.tribesoflagopus.backend.model.dto.LocationDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.LocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomService {

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;
  private final LocationRepository locationRepository;

  @Autowired
  public KingdomService(
      DtoService dtoService,
      KingdomRepository kingdomRepository,
      LocationRepository locationRepository) {

    this.dtoService = dtoService;
    this.kingdomRepository = kingdomRepository;
    this.locationRepository = locationRepository;
  }

  public KingdomDto createKingdomDto(Long userId) {

    Kingdom foundKingdom = kingdomRepository.findOneByUserId(userId);
    KingdomDto kingdomResponse = dtoService.convertFromKingdom(foundKingdom);

    return kingdomResponse;
  }

  public KingdomDto createModifiedKingdomDto(
      Long userId,
      KingdomInputModifyDto kingdomInputModifyDto) {

    Kingdom modifiedKingdom = saveAndReturnModifiedKingdom(userId, kingdomInputModifyDto);

    KingdomDto kingdomResponse = dtoService.convertFromKingdom(modifiedKingdom);

    return kingdomResponse;
  }

  private Kingdom saveAndReturnModifiedKingdom(Long userId, KingdomInputModifyDto kingdomInputModifyDto) {
    Kingdom foundKingdom = kingdomRepository.findOneByUserId(userId);

    String kingdomNewName = kingdomInputModifyDto.getName();
    LocationDto kingdomNewLocationDto = kingdomInputModifyDto.getLocation();

    if (kingdomNewName != null) {
      foundKingdom.setName(kingdomNewName);
    }

    if (kingdomNewLocationDto != null) {
      Integer x = kingdomNewLocationDto.getX();
      Integer y = kingdomNewLocationDto.getY();

      if (!locationRepository.existsByXAndY(x, y)) {
        foundKingdom.setLocation(Location.builder().x(x).y(y).build());
      }
    }

    return kingdomRepository.save(foundKingdom);
  }

  public Kingdom getKingdomOfUser(Long userId) {
    return kingdomRepository.findOneByUserId(userId);
  }

  public Kingdom saveKingdom (Kingdom kingdom) {
    return kingdomRepository.save(kingdom);
  }

  public List<Kingdom> findAllKingdoms(){
    return kingdomRepository.findAll();
  }

}