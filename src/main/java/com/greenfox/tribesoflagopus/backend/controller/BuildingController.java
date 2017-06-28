package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingTypeInputDto;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildingController {

  @Autowired
  BuildingService buildingService;

  @Autowired
  UserService userService;

  @Autowired
  ErrorService errorService;

  @GetMapping("/{userId}/kingdom/buildings")
  public ResponseEntity<JsonDto> getListOfBuildings(
      @Valid @PathVariable(value = "userId") long userId) {
    if (!userService.existsUserById(userId)) {
      StatusResponse userIdNotFoundStatus = errorService.getUserIdNotFoundStatus();
      return ResponseEntity.status(404).body(userIdNotFoundStatus);
    }
    BuildingListDto buildings = buildingService.createBuildingList(userId);
    return ResponseEntity.ok().body(buildings);
  }

  //Todo: handle null input
  //Todo: handle invalid buildingtype with annotation?
  @PostMapping("/{userId}/kingdom/buildings")
  public ResponseEntity<JsonDto> addNewBuildingToKingdom(
      @Valid @RequestBody BuildingTypeInputDto buildingTypeInputDto,
      BindingResult bindingResult, @Valid @PathVariable(value = "userId") long userId) {

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

    BuildingDto newBuildingDto = buildingService.addNewBuilding(buildingTypeInputDto.getType(), userId);
    return ResponseEntity.ok().body(newBuildingDto);
  }

}
