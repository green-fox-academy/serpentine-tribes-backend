package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.dto.StatusResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ErrorService {

  public StatusResponse getMissingParameterStatus(BindingResult bindingResult) {
    String statusMessage = createStatusMessageWithMissingFieldNames(bindingResult);
    StatusResponse missingParameterStatus = StatusResponse.builder()
        .status("error")
        .message("Missing parameter(s): " + statusMessage + "!")
        .build();
    return missingParameterStatus;
  }

  public String createStatusMessageWithMissingFieldNames(BindingResult bindingResult) {
    List<FieldError> missingFields = bindingResult.getFieldErrors();
    ArrayList<String> missingFieldNames = new ArrayList<>();
    for (FieldError fieldError : missingFields) {
      missingFieldNames.add(fieldError.getField());
    }
    Collections.sort(missingFieldNames);
    String statusMessage = String.join(", ", missingFieldNames);
    return statusMessage;
  }

  public StatusResponse getOccupiedUserNameStatus() {
    StatusResponse occupiedUserNameStatus = StatusResponse.builder()
        .status("error")
        .message("Username already taken, please choose an other one.")
        .build();
    return occupiedUserNameStatus;
  }

  public StatusResponse getIncorrectUserStatus(String incorrectUserName) {
    StatusResponse incorrectUser = StatusResponse.builder()
        .status("error")
        .message("No such user: " + incorrectUserName)
        .build();
    return incorrectUser;
  }

  public StatusResponse getIncorrectPasswordStatus() {
    StatusResponse incorrectPassword = StatusResponse.builder()
        .status("error")
        .message("Wrong password")
        .build();
    return incorrectPassword;
  }

  public StatusResponse getUserIdNotFoundStatus() {
    StatusResponse userIdNotFound = StatusResponse.builder()
        .status("error")
        .message("UserId not found")
        .build();
    return userIdNotFound;
  }

  public StatusResponse getUserIdWasNotRecoverableFromToken() {
    StatusResponse userIdNotFound = StatusResponse.builder()
        .status("error")
        .message("User ID not recoverable from token")
        .build();
    return userIdNotFound;
  }

  public StatusResponse getInvalidBuildingTypeStatus() {
    StatusResponse invalidBuildingType = StatusResponse.builder()
        .status("error")
        .message("Invalid building type!")
        .build();
    return invalidBuildingType;
  }

  public StatusResponse getInvalidIdStatus(Long id) {
    StatusResponse invalidId = StatusResponse.builder()
        .status("error")
        .message(String.format("Id: %d not found!", id))
        .build();
    return invalidId;
  }

  public StatusResponse getInvalidBuildingLevelStatus() {
    StatusResponse invalidBuildingLevel = StatusResponse.builder()
        .status("error")
        .message("Invalid building level!")
        .build();
    return invalidBuildingLevel;
  }

  public StatusResponse getTroopIdNotReceivedStatus() {
    StatusResponse troopIdNotReceived = StatusResponse.builder()
        .status("error")
        .message("Troop ID not received")
        .build();
    return troopIdNotReceived;
  }

  public StatusResponse getTroopIdNotFoundStatus() {
    StatusResponse troopIdNotFound = StatusResponse.builder()
        .status("error")
        .message("Troop ID not found")
        .build();
    return troopIdNotFound;
  }
}
