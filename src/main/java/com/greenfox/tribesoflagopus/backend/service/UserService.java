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
public class UserService {

  private final LocationService locationService;
  private final UserRepository userRepository;

  @Autowired
  public UserService(LocationService locationService, UserRepository userRepository) {
    this.locationService = locationService;
    this.userRepository = userRepository;
  }

  public boolean existsUserById(Long userId) {
    return userRepository.exists(userId);
  }

  public boolean existsUserByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public boolean isPasswordCorrect(String username, String password) {
    return password.equals(userRepository.findByUsername(username).getPassword());
  }

  public void register(UserRegisterInput registerInput) {

    UserRegisterInput processedRegisterInput = setDefaultKingdomNameIfNeeded(registerInput);

    User user = createUserWithKingdom(processedRegisterInput);
  }

  public User createUserWithKingdom(UserRegisterInput registerInput) {
    User user = User.builder()
        .username(registerInput.getUsername())
        .password(registerInput.getPassword())
        .points(0)
        .build();

    Kingdom kingdom = Kingdom.builder()
        .name(registerInput.getKingdom())
        .build();

    Location location = locationService.createNewValidLocation();

    kingdom.setLocation(location);
    location.setKingdom(kingdom);

    user.setKingdom(kingdom);
    kingdom.setUser(user);

    userRepository.save(user);
    return user;
  }

  private UserRegisterInput setDefaultKingdomNameIfNeeded(UserRegisterInput registerInput) {
    String inputKingdomName = registerInput.getKingdom();
    if (inputKingdomName == null || inputKingdomName.equals("")) {
      registerInput.setKingdom(String.format("%s's kingdom", registerInput.getUsername()));
    }
    return registerInput;
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
