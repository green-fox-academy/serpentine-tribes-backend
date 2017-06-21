package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.service.RegistrationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

  @Autowired
  RegistrationService registrationService;

  @PostMapping(value = "/register")
  public ResponseEntity<JsonDto> registerUser(
          @Valid UserRegisterInput registerInput,
          BindingResult bindingResult) {
    return registrationService.register(registerInput, bindingResult);
  }

}