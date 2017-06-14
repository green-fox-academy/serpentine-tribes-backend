package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Player;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

  @PostMapping("/login")
  public JsonDto loginUser(@Valid @RequestBody UserLoginInput loginInput, BindingResult bindingResult){

    if (bindingResult.hasErrors()) {
      return StatusResponse.builder()
              .status("error")
              .message("Missing username or password")
              .build();
    }

    if (!loginInput.getUsername().equals("Bond")){
      return StatusResponse.builder()
              .status("error")
              .message("No such user" + loginInput.getUsername())
              .build();
    }

    if (!loginInput.getPassword().equals("password123")){
      return StatusResponse.builder()
              .status("error")
              .message("Wrong password")
              .build();
    }

    return Player.builder()
            .id(1L)
            .username("Bond")
            .kingdomId(1L)
            .build();
  }
}
