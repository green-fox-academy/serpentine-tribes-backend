package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.service.BuildingService;
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

  @GetMapping("/{userId}/kingdom/buildings")
  public ResponseEntity<JsonDto> getListOfBuildings(
      @Valid @PathVariable(value = "userId") long userId) {
    return buildingService.createListOfBuildings(userId);
  }

}
