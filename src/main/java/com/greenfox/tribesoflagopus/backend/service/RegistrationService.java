package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Created by K on 2017.06.21..
 */
@Service
public class RegistrationService {

  public ResponseEntity<JsonDto> register(@Valid UserRegisterInput registerInput,
      BindingResult bindingResult) {

    registerInput = setKingdomName(registerInput);

    if (bindingResult.hasErrors()) {
      List<FieldError> missingFields = bindingResult.getFieldErrors();
      ArrayList<String> missingFieldNames = new ArrayList<>();
      for (FieldError fieldError : missingFields) {
        missingFieldNames.add(fieldError.getField());
      }
      Collections.sort(missingFieldNames);

      String statusMessage = String.join(", ", missingFieldNames);

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

  private UserRegisterInput setKingdomName(UserRegisterInput registerInput) {
    if (registerInput.getKingdom() == null || registerInput.getKingdom().equals("")) {
      registerInput.setKingdom(String.format("%s's kingdom", registerInput.getUsername()));
    }
    return registerInput;
  }


}