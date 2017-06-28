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

    UserDto dtoUserToReturn = createUserDto();
    return ResponseEntity.ok().body(dtoUserToReturn);
  }

  private boolean inputUserNameExists() {
    return userRepository.existsByUsername(inputUserName);
  }

  private boolean inputPasswordIsCorrect() {
    return inputPassword.equals(userRepository.findByUsername(inputUserName).getPassword());
  }

  private UserDto createUserDto() {
    User userToReturn = userRepository.findByUsername(inputUserName);
    UserDto dtoUserToReturn = UserDto.builder()
        .id(userToReturn.getId())
        .username(userToReturn.getUsername())
        .kingdomId(userToReturn.getKingdom().getId())
        .token(userToReturn.getToken())
        .build();
    return dtoUserToReturn;
  }
}
