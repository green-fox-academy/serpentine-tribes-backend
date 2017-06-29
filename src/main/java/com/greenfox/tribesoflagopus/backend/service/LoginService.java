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

  private String inputUserName;
  private String inputPassword;

  public void login(@Valid UserLoginInput loginInput,
                                       BindingResult bindingResult){

    inputUserName = loginInput.getUsername();
    inputPassword = loginInput.getPassword();
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
