package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  UserRepository userRepository;

  @Autowired
  ErrorService errorService;

  public ResponseEntity<JsonDto> login(@Valid UserLoginInput loginInput,
                                       BindingResult bindingResult){

    if(bindingResult.hasErrors()){
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    if (!userRepository.existsByUsername(loginInput.getUsername())) {
      StatusResponse incorrectUser = StatusResponse.builder()
              .status("error")
              .message("No such user: " + loginInput.getUsername())
              .build();
      return ResponseEntity.status(401).body(incorrectUser);
    }

    if (!loginInput.getPassword()
            .equals(userRepository
                    .findByUsername(loginInput.getUsername())
                    .getPassword())) {
      StatusResponse incorrectPassword = StatusResponse.builder()
              .status("error")
              .message("Wrong password")
              .build();
      return ResponseEntity.status(401).body(incorrectPassword);
    }

    User userToReturn = userRepository.findByUsername(loginInput.getUsername());
    UserDto dtoUserToReturn = UserDto.builder()
            .id(userToReturn.getId())
            .username(userToReturn.getUsername())
            .kingdomId(userToReturn.getKingdom().getId())
            .build();
    return ResponseEntity.ok().body(dtoUserToReturn);
  }
}
