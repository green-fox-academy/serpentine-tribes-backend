package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingLevelInputDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingTypeInputDto;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class BuildingController {

  private final BuildingService buildingService;
  private final UserService userService;
  private final ErrorService errorService;
  private final TokenService tokenService;

  @Autowired
  public BuildingController(
      BuildingService buildingService,
      UserService userService,
      ErrorService errorService,
      TokenService tokenService) {
    this.buildingService = buildingService;
    this.userService = userService;
    this.errorService = errorService;
    this.tokenService = tokenService;
  }

  @GetMapping("/kingdom/buildings")
  public ResponseEntity<JsonDto> getListOfBuildings(
      @RequestHeader(value = "X-tribes-token") String token) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (!userService.existsUserById(userId)) {
      StatusResponse userIdNotFoundStatus = errorService.getUserIdNotFoundStatus();
      return ResponseEntity.status(404).body(userIdNotFoundStatus);
    }
    BuildingListDto buildings = buildingService.getBuildingList(userId);
    return ResponseEntity.ok().body(buildings);
  }

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<JsonDto> addNewBuildingToKingdom(
      @Valid @RequestBody BuildingTypeInputDto buildingTypeInputDto,
      BindingResult bindingResult,
      @RequestHeader(value = "X-tribes-token") String token) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (bindingResult.hasErrors()) {
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    } else if (!userService.existsUserById(userId)) {
      StatusResponse userIdNotFoundStatus = errorService.getUserIdNotFoundStatus();
      return ResponseEntity.status(404).body(userIdNotFoundStatus);
    } else if (!buildingService.validBuildingType(buildingTypeInputDto.getType())) {
      StatusResponse invalidBuildingTypeStatus = errorService.getInvalidBuildingTypeStatus();
      return ResponseEntity.badRequest().body(invalidBuildingTypeStatus);
    }

    BuildingDto newBuildingDto = buildingService
        .addNewBuilding(buildingTypeInputDto.getType(), userId);
    return ResponseEntity.ok().body(newBuildingDto);
  }

  @PutMapping("/kingdom/buildings/{buildingId}")
  public ResponseEntity<JsonDto> updateBuildingLevel(
      @Valid @RequestBody BuildingLevelInputDto buildingLevelInputDto,
      BindingResult bindingResult,
      @RequestHeader(value = "X-tribes-token") String token,
      @Valid @PathVariable(value = "buildingId") Long buildingId) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    if (bindingResult.hasErrors()) {
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    } else if (!userService.existsUserById(userId)) {
      StatusResponse invalidIdStatus = errorService.getInvalidIdStatus(userId);
      return ResponseEntity.status(404).body(invalidIdStatus);
    } else if (!buildingService.existsBuildingById(buildingId)) {
      StatusResponse invalidIdStatus = errorService.getInvalidIdStatus(buildingId);
      return ResponseEntity.status(404).body(invalidIdStatus);
    } else if (buildingLevelInputDto.getLevel() < 1) {
      StatusResponse invalidBuildingLevel = errorService.getInvalidBuildingLevelStatus();
      return ResponseEntity.badRequest().body(invalidBuildingLevel);
    }

    BuildingDto updatedBuildingDto = buildingService
        .updateBuilding(buildingId, buildingLevelInputDto);
    return ResponseEntity.ok().body(updatedBuildingDto);
  }

}
