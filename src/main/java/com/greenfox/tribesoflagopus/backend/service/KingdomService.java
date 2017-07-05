package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomInputModifyDto;
import com.greenfox.tribesoflagopus.backend.model.dto.LocationDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.LocationRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KingdomService {

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;
  private final LocationRepository locationRepository;
  private final UserRepository userRepository;

  @Autowired
  public KingdomService(
      DtoService dtoService,
      KingdomRepository kingdomRepository,
      LocationRepository locationRepository,
      UserRepository userRepository) {
    this.dtoService = dtoService;
    this.kingdomRepository = kingdomRepository;
    this.locationRepository = locationRepository;
    this.userRepository = userRepository;
  }

  public ResponseEntity<JsonDto> showKingdom(Long userId) {

    if (!userRepository.exists(userId)) {
      StatusResponse userNotFoundStatus = StatusResponse.builder()
          .status("error")
          .message("user_id not found")
          .build();
      return ResponseEntity.status(404).body(userNotFoundStatus);
    }

    Kingdom foundKingdom = kingdomRepository.findOneByUserId(userId);
    KingdomDto kingdomResponse = dtoService.convertFromKingdom(foundKingdom);

    return ResponseEntity.ok().body(kingdomResponse);
  }

  public ResponseEntity<JsonDto> modifyKingdom(
      Long userId,
      KingdomInputModifyDto kingdomInputModifyDto) {

    if (!userRepository.exists(userId)) {
      StatusResponse userNotFoundStatus = StatusResponse.builder()
          .status("error")
          .message("user_id not found")
          .build();
      return ResponseEntity.status(404).body(userNotFoundStatus);
    }

    Kingdom modifiedKingdom = saveAndReturnModifiedKingdom(userId, kingdomInputModifyDto);

    KingdomDto kingdomResponse = dtoService.convertFromKingdom(modifiedKingdom);

    return ResponseEntity.ok().body(kingdomResponse);
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

  public Kingdom findKingdomByUserId(Long userId) {
    return kingdomRepository.findOneByUserId(userId);
  }

  public List<Building> getBuildingsByUserId(Long userId) {
    return findKingdomByUserId(userId).getBuildings();
  }
}