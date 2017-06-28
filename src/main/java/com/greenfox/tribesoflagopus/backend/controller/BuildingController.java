package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    if(!userService.existsUserById(userId)) {
      StatusResponse userIdNotFoundStatus = errorService.getUserIdNotFoundStatus();
      return ResponseEntity.status(404).body(userIdNotFoundStatus);
    }
    BuildingListDto buildings = buildingService.createBuildingList(userId);
    return ResponseEntity.ok().body(buildings);
  }

}
