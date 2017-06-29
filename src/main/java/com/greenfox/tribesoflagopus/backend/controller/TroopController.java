package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.TroopService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroopController {

  private final ErrorService errorService;
  private final TokenService tokenService;
  private final TroopService troopService;
  private final UserService userService;

  @Autowired
  public TroopController(ErrorService errorService,
      TokenService tokenService,
      TroopService troopService,
      UserService userService) {

    this.errorService = errorService;
    this.tokenService = tokenService;
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
  public ResponseEntity<JsonDto> showOneTroop(@PathVariable Long userId,
      @PathVariable Long troopId) {

    if (troopService.existsByIdAndUserId(troopId, userId)) {
      return ResponseEntity.ok().body(troopService.fetchTroop(userId, troopId));

    } else {
//      TODO create proper error message after the API specification is clear on this.
      return ResponseEntity.status(404)
          .body(StatusResponse.builder().status("error").message("<id> not found").build());
    }
  }

  @PostMapping(value = "/kingdom/troops")
  public ResponseEntity<JsonDto> createNewTroop(
      @RequestHeader(value = "X-tribes-token") String token) {

    Long userId = tokenService.getIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (!userService.existsUserById(userId)) {
      return ResponseEntity.status(404).body(errorService.getUserIdNotFoundStatus());
    }

    TroopDto addedTroopDto = troopService.addNewTroop(userId);
    return ResponseEntity.ok().body(addedTroopDto);
  }
}
