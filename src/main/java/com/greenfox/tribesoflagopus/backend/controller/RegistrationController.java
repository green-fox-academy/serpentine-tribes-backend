package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Player;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

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
      String statusMessage = "";
      for (int i = 0; i < missingFields.size(); i++) {
        if(i > 0) {
          statusMessage += ", ";
        }
        statusMessage += missingFields.get(i).getField();
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

    Player mockPlayer = Player.builder()
        .id(1L)
        .username("Bond")
        .kingdomId(1L)
        .build();
    return ResponseEntity.ok().body(mockPlayer);
  }

}
