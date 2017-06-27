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

  private final DtoService dtoService;
  private final KingdomRepository kingdomRepository;
  private final UserRepository userRepository;

  @Autowired
  public KingdomService(
      DtoService dtoService,
      KingdomRepository kingdomRepository,
      UserRepository userRepository) {
    this.dtoService = dtoService;
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
    KingdomDto kingdomResponse = dtoService.convertFromKingdom(foundKingdom);

    return ResponseEntity.ok().body(kingdomResponse);
  }

  public ResponseEntity<JsonDto> modifyKingdom(
      Long userId,
      KingdomDto kingdomDto) {

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
}