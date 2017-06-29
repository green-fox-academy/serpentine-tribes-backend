package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.service.ErrorService;
import com.greenfox.tribesoflagopus.backend.service.RegistrationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

  private final RegistrationService registrationService;
  private final ErrorService errorService;

  @Autowired
  public RegistrationController(
          RegistrationService registrationService, ErrorService errorService) {
    this.registrationService = registrationService;
    this.errorService = errorService;
  }

  @PostMapping(value = "/register")
  public ResponseEntity<JsonDto> registerUser(
      @RequestBody @Valid UserRegisterInput registerInput,
          BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      StatusResponse missingParameterStatus = errorService.getMissingParameterStatus(bindingResult);
      return ResponseEntity.badRequest().body(missingParameterStatus);
    }
    
    return registrationService.register(registerInput, bindingResult);
  }

}