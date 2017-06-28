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

  @Autowired
  UserService userService;

  private String inputUserName;
  private String inputPassword;

  public ResponseEntity<JsonDto> login(@Valid UserLoginInput loginInput,
                                       BindingResult bindingResult){

    if(bindingResult.hasErrors()){
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    inputUserName = loginInput.getUsername();
    inputPassword = loginInput.getPassword();

    if (!inputUserNameExists()) {
      StatusResponse incorrectUser = errorService.getIncorrectUserStatus(loginInput.getUsername());
      return ResponseEntity.status(401).body(incorrectUser);
    }

    if (!inputPasswordIsCorrect()) {
      StatusResponse incorrectPassword = errorService.getIncorrectPasswordStatus();
      return ResponseEntity.status(401).body(incorrectPassword);
    }

    tokenService.saveTokenToUser(userRepository.findByUsername(inputUserName));
    UserTokenDto userTokenDto = createUserTokenDto();
    return ResponseEntity.ok().body(userTokenDto);
  }

  private boolean inputUserNameExists() {
    return userRepository.existsByUsername(inputUserName);
  }

  private boolean inputPasswordIsCorrect() {
    return inputPassword.equals(userRepository.findByUsername(inputUserName).getPassword());
  }

  private UserTokenDto createUserTokenDto() {
    User userToReturn = userRepository.findByUsername(inputUserName);
    UserTokenDto userTokenDtoReturn = UserTokenDto.builder()
        .status("ok")
        .token(userToReturn.getToken())
        .build();
    return userTokenDtoReturn;
  }
}
