package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.UserDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  LocationService locationService;

  private String inputUsername;
  private String inputPassword;
  private String kingdomName;

  public void register(UserRegisterInput registerInput) {

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

    Location location = locationService.createNewValidLocation();

    kingdom.setLocation(location);
    location.setKingdom(kingdom);

    user.setKingdom(kingdom);
    kingdom.setUser(user);

    userRepository.save(user);
    return user;
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
