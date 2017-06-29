package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.TroopService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroopController {

  private final ErrorService errorService;
  private final TroopService troopService;
  private final UserService userService;

  @Autowired
  public TroopController(ErrorService errorService,
      TroopService troopService,
      UserService userService) {

    this.errorService = errorService;
    this.troopService = troopService;
    this.userService = userService;
  }

  @GetMapping(value = "/{userId}/kingdom/troops")
  public ResponseEntity<JsonDto> listTroops(@PathVariable Long userId) {

    if (!userService.existsUserById(userId)) {
      return ResponseEntity.status(404).body(errorService.getUserIdNotFoundStatus());
    }

    return ResponseEntity.ok().body(troopService.listTroopsOfUser(userId));
  }

  @GetMapping(value = "/{userId}/kingdom/troops/{troopId}")
  public ResponseEntity<JsonDto> showOneTroop(@PathVariable Long userId, @PathVariable Long troopId) {

    if (troopService.existsByIdAndUserId(troopId, userId)) {
      return ResponseEntity.ok().body(troopService.fetchTroop(userId, troopId));

    } else {
//      TODO create proper error message after the API specification is clear on this.
      return ResponseEntity.status(404).body(StatusResponse.builder().status("error").message("<id> not found").build());
    }
  }
}
