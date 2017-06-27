package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomDto;
import com.greenfox.tribesoflagopus.backend.service.KingdomService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {

  private final KingdomService kingdomService;

  @Autowired
  public KingdomController(KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  @GetMapping("{userId}/kingdom")
  public ResponseEntity<JsonDto> showKingdom(@PathVariable Long userId) {
    return kingdomService.showKingdom(userId);
  }

  @PutMapping(value = "{userId}/kingdom",
      consumes = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_JSON_UTF8_VALUE})
  public ResponseEntity<JsonDto> modifyKingdom(
      @PathVariable Long userId,
      @Valid @RequestBody KingdomDto kingdomDto) {
    return kingdomService.modifyKingdom(userId, kingdomDto);
  }

}