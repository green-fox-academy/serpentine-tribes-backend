package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.LocationRepository;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class RegistrationService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  LocationRepository locationRepository;

  public ResponseEntity<JsonDto> register(@Valid UserRegisterInput registerInput,
      BindingResult bindingResult) {

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

    if (registerInput == null) {
      StatusResponse missingAllFields = StatusResponse.builder()
          .status("error")
          .message("Missing parameter(s): password, username!")
          .build();
      return ResponseEntity.badRequest().body(missingAllFields);
    }

    /*
    if ("occupiedUserName".equals(registerInput.getUsername())) {
      StatusResponse occupiedUserNameStatus = StatusResponse.builder()
          .status("error")
          .message("Username already taken, please choose an other one.")
          .build();
      return ResponseEntity.status(409).body(occupiedUserNameStatus);
    }
    */


    if(userRepository.existsByUsername(registerInput.getUsername())) {
      StatusResponse occupiedUserNameStatus = StatusResponse.builder()
          .status("error")
          .message("Username already taken, please choose an other one.")
          .build();
      return ResponseEntity.status(409).body(occupiedUserNameStatus);
    }

    registerInput = setKingdomName(registerInput);

    //TODO: create user and kingdom
    //TODO: set proper default values

    User user = User.builder()
        .username(registerInput.getUsername())
        .password(registerInput.getPassword())
        .points(0)
        .build();

    Kingdom kingdom = Kingdom.builder()
        .name(registerInput.getKingdom())
        .build();

    Location location = generateRandomLocation();
    kingdom.setLocation(location);
    location.setKingdom(kingdom);

    user.setKingdom(kingdom);
    kingdom.setUser(user);

    //TODO: save this in db -see autowired above
    userRepository.save(user);


    //TODO: to use userdto is OK?
    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .kingdomId(user.getKingdom().getId())
        .build();
    return ResponseEntity.ok().body(userDto);
  }

  private UserRegisterInput setKingdomName(UserRegisterInput registerInput) {
    if (registerInput.getKingdom() == null || registerInput.getKingdom().equals("")) {
      registerInput.setKingdom(String.format("%s's kingdom", registerInput.getUsername()));
    }
    return registerInput;
  }

  //Todo: is there a simpler way?
  private Location generateRandomLocation() {
    Location location = new Location();
    Integer locationX;
    Integer locationY;
    do {
      locationX = generateRandomNumber(1, 100);
      locationY = generateRandomNumber(1, 100);
    } while(locationRepository.existsByXAndY(locationX, locationY));
    location.setX(locationX);
    location.setY(locationY);
    return location;
  }

  private Integer generateRandomNumber(Integer min, Integer max) {
    int random = min + (int) (Math.random() * (max + 1));
    Integer randomNumber = Integer.valueOf(random);
    return randomNumber;
  }


}
