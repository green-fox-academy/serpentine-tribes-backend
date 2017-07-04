package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import com.greenfox.tribesoflagopus.backend.service.DtoService;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.TokenService;
import com.greenfox.tribesoflagopus.backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class LoginController {

  private final DtoService dtoService;
  private final ErrorService errorService;
  private final TokenService tokenService;
  private final UserService userService;

  @Autowired
  public LoginController(
      DtoService dtoService,
      ErrorService errorService,
      TokenService tokenService,
      UserService userService) {

    this.dtoService = dtoService;
    this.errorService = errorService;
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<JsonDto> loginUser(
          @Valid @RequestBody UserLoginInput userLoginInput,
          BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }

    if (!userService.existsUserByUsername(userLoginInput.getUsername())) {
      StatusResponse incorrectUser = errorService.getIncorrectUserStatus(userLoginInput.getUsername());
      return ResponseEntity.status(401).body(incorrectUser);
    }

    if (!userService.isPasswordCorrect(userLoginInput.getUsername(), userLoginInput.getPassword())) {
      StatusResponse incorrectPassword = errorService.getIncorrectPasswordStatus();
      return ResponseEntity.status(401).body(incorrectPassword);
    }

    String savedToken = tokenService.saveNewTokenToUser(userLoginInput.getUsername());
    return ResponseEntity.ok().body(dtoService.createUserTokenDto(savedToken));
  }
}
