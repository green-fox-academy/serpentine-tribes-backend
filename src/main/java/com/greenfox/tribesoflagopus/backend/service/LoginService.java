package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.*;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

@Service
public class LoginService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ErrorService errorService;

  @Autowired
  TokenService tokenService;

  public boolean inputPasswordIsCorrect(String username, String password) {
    return password.equals(userRepository.findByUsername(username).getPassword());
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
