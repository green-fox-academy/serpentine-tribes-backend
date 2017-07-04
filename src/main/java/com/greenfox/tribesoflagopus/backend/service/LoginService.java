package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.UserTokenDto;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final UserRepository userRepository;

  @Autowired
  public LoginService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserTokenDto createUserTokenDto(String username) {
    User userToReturn = userRepository.findByUsername(username);
    UserTokenDto userTokenDtoReturn = UserTokenDto.builder()
        .status("ok")
        .token(userToReturn.getToken())
        .build();
    return userTokenDtoReturn;
  }
}
