package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LoginService {

  public ResponseEntity<JsonDto> login(@Valid UserLoginInput loginInput,
                                       BindingResult bindingResult){

    if(bindingResult.hasErrors()){
      List<FieldError> listOfMissingFields = bindingResult.getFieldErrors();
      ArrayList<String> missingFields = new ArrayList<>();
      for (FieldError fieldError : listOfMissingFields) {
        missingFields.add(fieldError.getField());
      }
      Collections.sort(missingFields);

      String error = String.join(", ", missingFields);

      StatusResponse missingParameterStatus = StatusResponse.builder()
              .status("error")
              .message("Missing parameter(s): " + error + "!")
              .build();
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    if (loginInput == null) {
      StatusResponse missingAllFields = StatusResponse.builder()
              .status("error")
              .message("Missing parameter(s): password, username!")
              .build();
      return ResponseEntity.badRequest().body(missingAllFields);
    }

    if (!"Bond".equals(loginInput.getUsername())) {
      StatusResponse incorrectUser = StatusResponse.builder()
              .status("error")
              .message("No such user: " + loginInput.getUsername())
              .build();
      return ResponseEntity.status(401).body(incorrectUser);
    }

    if (!"password123".equals(loginInput.getPassword())) {
      StatusResponse incorrectPassword = StatusResponse.builder()
              .status("error")
              .message("Wrong password")
              .build();
      return ResponseEntity.status(401).body(incorrectPassword);
    }

    UserDto mockUser = UserDto.builder()
        .id(1)
        .username("Bond")
        .kingdomId(1)
        .build();
    return ResponseEntity.ok().body(mockUser);
  }
}
