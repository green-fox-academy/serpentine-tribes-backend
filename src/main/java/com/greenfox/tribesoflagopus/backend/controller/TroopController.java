package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import com.greenfox.tribesoflagopus.backend.model.dto.TroopLevelInputDto;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.TroopService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class TroopController {

  private final ErrorService errorService;
  private final TokenService tokenService;
  private final TroopService troopService;
  private final UserService userService;
  private final BuildingService buildingService;

  @Autowired
  public TroopController(ErrorService errorService,
      TokenService tokenService,
      TroopService troopService,
      UserService userService,
      BuildingService buildingService) {

    this.errorService = errorService;
    this.tokenService = tokenService;
    this.troopService = troopService;
    this.userService = userService;
    this.buildingService = buildingService;
  }

  @GetMapping(value = "/kingdom/troops")
  public ResponseEntity<JsonDto> listTroops(@RequestHeader(value = "X-tribes-token") String token) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (!userService.existsUserById(userId)) {
      return ResponseEntity.status(404).body(errorService.getUserIdNotFoundStatus());
    }

    return ResponseEntity.ok().body(troopService.listTroopsOfUser(userId));
  }

  @GetMapping(value = "/kingdom/troops/{troopId}")
  public ResponseEntity<JsonDto> showOneTroop(@RequestHeader(value = "X-tribes-token") String token,
      @PathVariable Long troopId) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (troopService.existsByIdAndUserId(troopId, userId)) {
      return ResponseEntity.ok().body(troopService.fetchTroop(userId, troopId));

    } else {
      StatusResponse troopIdNotFound = errorService.getTroopIdNotFoundStatus();
      return ResponseEntity.status(404).body(troopIdNotFound);
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

    if(!buildingService.hasUserBuildingType(BuildingType.BARRACK, userId)) {
      return ResponseEntity.badRequest().body(errorService.getNoBarrackStatus());
    }

    if(!troopService.hasEnoughGoldForNewTroop(userId)) {
      StatusResponse notEnoughGoldStatus = errorService.getNotEnoughGoldStatus();
      return ResponseEntity.badRequest().body(notEnoughGoldStatus);
    }

    TroopDto addedTroopDto = troopService.addNewTroop(userId);
    return ResponseEntity.ok().body(addedTroopDto);
  }

  @PutMapping("/kingdom/troops/{troopId}")
  public ResponseEntity<JsonDto> updateTroopLevel(
      @Valid @RequestBody TroopLevelInputDto troopLevelInputDto,
      BindingResult bindingResult,
      @RequestHeader(value = "X-tribes-token") String token,
      @Valid @PathVariable(value = "troopId") Long troopId) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (bindingResult.hasErrors()) {
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    } else if (!troopService.existsByTroopIdAndUserId(troopId, userId)) {
      StatusResponse invalidIdStatus = errorService.getInvalidIdStatus(troopId);
      return ResponseEntity.status(404).body(invalidIdStatus);
    } else if (troopLevelInputDto.getLevel() < 1) {
      StatusResponse invalidTroopLevel = errorService.getInvalidTroopLevelStatus();
      return ResponseEntity.badRequest().body(invalidTroopLevel);
    }

    TroopDto updatedTroopDto = troopService.updateTroop(troopId, troopLevelInputDto.getLevel());
    return ResponseEntity.ok().body(updatedTroopDto);
  }
}
