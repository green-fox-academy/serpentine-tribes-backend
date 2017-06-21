package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Player;
import com.greenfox.tribesoflagopus.backend.repository.PlayerRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

  private final PlayerRepository playerRepository;

  @Autowired
  public RegistrationController(
          PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @PostMapping(value = "/register")
  public ResponseEntity<JsonDto> registerUser(
          @Valid UserRegisterInput registerInput,
          BindingResult bindingResult) {

    if (registerInput.getKingdom() == null
            || registerInput.getKingdom().equals("")) {
      registerInput.setKingdom(
              String.format("%s's kingdom",
                      registerInput.getUsername()));
    }

    if (bindingResult.hasErrors()) {

      List<FieldError> missingFields = bindingResult.getFieldErrors();
      ArrayList<String> missingFieldNames = new ArrayList<>();

      for (FieldError fieldError : missingFields) {
        missingFieldNames.add(fieldError.getField());
      }
      Collections.sort(missingFieldNames);

      String statusMessage = "";
      for (int i = 0; i < missingFields.size(); i++) {
        if(i > 0) {
          statusMessage += ", ";
        }
        statusMessage += missingFieldNames.get(i);
      }

      StatusResponse missingParameterStatus = StatusResponse.builder()
              .status("error")
              .message("Missing parameter(s): " + statusMessage + "!")
              .build();
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    if (registerInput.getUsername().equals("occupiedUserName")) {
      StatusResponse occupiedUserNameStatus = StatusResponse.builder()
              .status("error")
              .message("Username already taken, please choose an other one.")
              .build();
      return ResponseEntity.status(409).body(occupiedUserNameStatus);
    }

    Player mockPlayer = playerRepository.findOne(1L);
    return ResponseEntity.ok().body(mockPlayer);
  }

}