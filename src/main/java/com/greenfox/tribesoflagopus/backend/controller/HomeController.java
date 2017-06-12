package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterInput;
import com.greenfox.tribesoflagopus.backend.model.dto.UserRegisterResponse;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/")
  public String greeting() {
    return "hello, CircleCI!";
  }

  @PostMapping(value = "/register")
  public JsonDto registerUser(
          @Valid UserRegisterInput registerInput,
          BindingResult bindingResult) {

    if (registerInput.getKingdom() == null
            || registerInput.getKingdom().equals("")) {

      registerInput.setKingdom(
              String.format("%s's kingdom",
                      registerInput.getUsername()));
    }

    if (bindingResult.hasErrors()) {
      return StatusResponse.builder()
              .status("error")
              .message("Missing parameter(s):")
              .build();
    }

    return UserRegisterResponse.builder()
            .id(1L)
            .username("Bond")
            .kingdomId(1L)
            .build();
  }
}
