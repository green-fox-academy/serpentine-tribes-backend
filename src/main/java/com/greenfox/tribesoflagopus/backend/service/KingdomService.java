package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.model.dto.LocationDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.repository.KingdomRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KingdomService {

  private final KingdomRepository kingdomRepository;
  private final UserRepository userRepository;

  @Autowired
  public KingdomService(
      KingdomRepository kingdomRepository,
      UserRepository userRepository) {
    this.kingdomRepository = kingdomRepository;
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

    LocationDto locationOfKingdom = LocationDto.builder()
        .x(foundKingdom.getLocation().getX())
        .y(foundKingdom.getLocation().getY())
        .build();

    KingdomDto kingdomResponse = KingdomDto.builder()
        .id(foundKingdom.getId())
        .name(foundKingdom.getName())
        .userId(foundKingdom.getUser().getId())
        .buildings(foundKingdom.getBuildings())
        .resources(foundKingdom.getResources())
        .troops(foundKingdom.getTroops())
        .location(locationOfKingdom)
        .build();

    return ResponseEntity.ok().body(kingdomResponse);
  }
}
