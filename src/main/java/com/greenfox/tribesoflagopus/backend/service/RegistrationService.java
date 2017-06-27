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
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class RegistrationService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  ErrorService errorService;

  private String username;
  private String password;
  private String kingdomName;

  public ResponseEntity<JsonDto> register(@Valid UserRegisterInput registerInput,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    if (registerInput == null) {
      StatusResponse missingAllFields = StatusResponse.builder()
          .status("error")
          .message("Missing parameter(s): password, username!")
          .build();
      return ResponseEntity.badRequest().body(missingAllFields);
    }

    username = registerInput.getUsername();
    password = registerInput.getPassword();
    setKingdomName(registerInput);

    if (occupiedUserName()) {
      StatusResponse occupiedUserNameStatus = errorService.getOccupiedUserNameStatus();
      return ResponseEntity.status(409).body(occupiedUserNameStatus);
    }

    User user = createUserWithKingdom();
    return createUserDto(user);
  }

  private void setKingdomName(UserRegisterInput registerInput) {
    if (registerInput.getKingdom() == null || registerInput.getKingdom().equals("")) {
      kingdomName = String.format("%s's kingdom", username);
    } else {
      kingdomName = registerInput.getKingdom();
    }
  }

  private boolean occupiedUserName() {
    return userRepository.existsByUsername(username);
  }

  private User createUserWithKingdom() {
    User user = User.builder()
        .username(username)
        .password(password)
        .points(0)
        .build();

    Kingdom kingdom = Kingdom.builder()
        .name(kingdomName)
        .build();

    Location location = generateRandomLocation();

    kingdom.setLocation(location);
    location.setKingdom(kingdom);

    user.setKingdom(kingdom);
    kingdom.setUser(user);

    userRepository.save(user);

    return user;
  }

  private Location generateRandomLocation() {
    Location location = new Location();
    do {
      location.setX(generateRandomNumber(1, 100));
      location.setY(generateRandomNumber(1, 100));
    } while (locationRepository.existsByXAndY(location.getX(), location.getY()));
    return location;
  }

  private Integer generateRandomNumber(int min, int max) {
    int random = min + (int) (Math.random() * (max + 1));
    Integer randomNumber = Integer.valueOf(random);
    return randomNumber;
  }

  private ResponseEntity<JsonDto> createUserDto(User user) {
    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .kingdomId(user.getKingdom().getId())
        .build();
    return ResponseEntity.ok().body(userDto);
  }


}
