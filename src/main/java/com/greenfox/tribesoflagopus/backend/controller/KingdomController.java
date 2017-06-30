package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomInputModifyDto;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.KingdomService;
import javax.validation.Valid;

import com.greenfox.tribesoflagopus.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class KingdomController {

  private final ErrorService errorService;
  private final KingdomService kingdomService;
  private final TokenService tokenService;

  @Autowired
  public KingdomController(
      ErrorService errorService,
      KingdomService kingdomService,
      TokenService tokenService) {
    this.errorService = errorService;
    this.kingdomService = kingdomService;
    this.tokenService = tokenService;
  }

  @GetMapping("/kingdom")
  public ResponseEntity<JsonDto> showKingdom(@RequestHeader(value = "X-tribes-token") String token) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    return kingdomService.showKingdom(userId);
  }

  @PutMapping(value = "/kingdom",
      consumes = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_JSON_UTF8_VALUE})
  public ResponseEntity<JsonDto> modifyKingdom(
          @RequestHeader(value = "X-tribes-token") String token,
      @Valid @RequestBody KingdomInputModifyDto kingdomInputModifyDto) {

    Long userId = tokenService.getIdFromToken(token);
    if (userId == null) {
      return ResponseEntity.badRequest().body(errorService.getUserIdWasNotRecoverableFromToken());
    }

    return kingdomService.modifyKingdom(userId, kingdomInputModifyDto);
  }

}