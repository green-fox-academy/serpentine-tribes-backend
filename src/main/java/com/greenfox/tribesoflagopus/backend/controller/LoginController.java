package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;

import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import com.greenfox.tribesoflagopus.backend.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class LoginController {

  @Autowired
  LoginService loginService;

  @PostMapping("/login")
  public ResponseEntity<JsonDto> loginUser(
          @Valid UserLoginInput userLoginInput,
          BindingResult bindingResult) {
    return loginService.login(userLoginInput, bindingResult);
  }
}
