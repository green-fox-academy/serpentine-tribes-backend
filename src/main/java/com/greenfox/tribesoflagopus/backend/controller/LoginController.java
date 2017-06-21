package com.greenfox.tribesoflagopus.backend.controller;

import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import com.greenfox.tribesoflagopus.backend.model.dto.UserLoginInput;
import com.greenfox.tribesoflagopus.backend.model.entity.Player;
import com.greenfox.tribesoflagopus.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class LoginController {

  private final PlayerRepository playerRepository;

  @Autowired
  public LoginController(
          PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @PostMapping("/login")
  public JsonDto loginUser(@Valid UserLoginInput loginInput, BindingResult bindingResult){

    List<String> listOfMissingFields = new ArrayList<>();
    String errors = "";

    for (FieldError fielderror : bindingResult.getFieldErrors()) {
      listOfMissingFields.add(fielderror.getField());
    }
    Collections.sort(listOfMissingFields);

    for (int i = 0; i < listOfMissingFields.size(); i++) {
        errors += listOfMissingFields.get(i) + ", ";
    }

    if (bindingResult.hasErrors()) {
      return StatusResponse.builder()
              .status("error")
              .message("Missing parameter(s): " + errors)
              .build();
    }

    if (!loginInput.getUsername().equals("Bond")){
      return StatusResponse.builder()
              .status("error")
              .message("No such user: " + loginInput.getUsername())
              .build();
    }

    if (!loginInput.getPassword().equals("password123")){
      return StatusResponse.builder()
              .status("error")
              .message("Wrong password")
              .build();
    }

    Player mockPlayer = playerRepository.findOne(1L);
    return mockPlayer;
  }
}
