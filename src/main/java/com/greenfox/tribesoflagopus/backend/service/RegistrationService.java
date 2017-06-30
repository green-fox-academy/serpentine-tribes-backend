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

  private String inputUsername;
  private String inputPassword;
  private String kingdomName;
  private final Integer locationMinValue = 1;
  private final Integer locationMaxValue = 100;

  public void register(@Valid UserRegisterInput registerInput,
      BindingResult bindingResult) {

    inputUsername = registerInput.getUsername();
    inputPassword = registerInput.getPassword();
    updateKingdomName(registerInput);

    User user = createUserWithKingdom();
  }

  private void updateKingdomName(UserRegisterInput registerInput) {
    String inputKingdomName = registerInput.getKingdom();
    if (inputKingdomName == null || inputKingdomName.equals("")) {
      kingdomName = String.format("%s's kingdom", inputUsername);
    } else {
      kingdomName = inputKingdomName;
    }
  }

  private User createUserWithKingdom() {
    User user = User.builder()
        .username(inputUsername)
        .password(inputPassword)
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
      location.setX(generateRandomNumber(locationMinValue, locationMaxValue));
      location.setY(generateRandomNumber(locationMinValue, locationMaxValue));
    } while (locationRepository.existsByXAndY(location.getX(), location.getY()));
    return location;
  }

  private Integer generateRandomNumber(int min, int max) {
    int random = min + (int) (Math.random() * (max + 1));
    Integer randomNumber = Integer.valueOf(random);
    return randomNumber;
  }

  public UserDto createUserDto(String username) {
    User user = userRepository.findByUsername(username);
    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .kingdomId(user.getKingdom().getId())
        .build();
    return userDto;
  }
}
