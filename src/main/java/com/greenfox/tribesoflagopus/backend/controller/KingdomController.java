package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.KingdomInputModifyDto;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.KingdomService;
import javax.validation.Valid;

import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class KingdomController {

  private final UserService userService;
  private final ErrorService errorService;
  private final KingdomService kingdomService;
  private final TokenService tokenService;

  @Autowired
  public KingdomController(UserService userService,
      ErrorService errorService,
      KingdomService kingdomService,
      TokenService tokenService) {
    this.userService = userService;
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

    if (!userService.existsUserById(userId)) {
      return ResponseEntity.status(404).body(errorService.getUserNotFoundStatus());
    }

    return ResponseEntity.ok().body(kingdomService.createKingdomDto(userId));
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

    if (!userService.existsUserById(userId)) {
      return ResponseEntity.status(404).body(errorService.getUserNotFoundStatus());
    }

    return ResponseEntity.ok().body(kingdomService.createModifiedKingdomDto(userId, kingdomInputModifyDto));
  }

}