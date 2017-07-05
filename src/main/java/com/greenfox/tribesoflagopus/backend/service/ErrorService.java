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
    return StatusResponse.error("Missing parameter(s): " + statusMessage + "!");
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
    return StatusResponse.error("Username already taken, please choose an other one.");
  }

  public StatusResponse getIncorrectUserStatus(String incorrectUserName) {
    return StatusResponse.error("No such user: " + incorrectUserName);
  }

  public StatusResponse getIncorrectPasswordStatus() {
    return StatusResponse.error("Wrong password");
  }

  public StatusResponse getUserIdNotFoundStatus() {
    return StatusResponse.error("UserId not found");
  }

  public StatusResponse getUserIdWasNotRecoverableFromToken() {
    return StatusResponse.error("User ID not recoverable from token");
  }

  public StatusResponse getInvalidBuildingTypeStatus() {
    return StatusResponse.error("Invalid building type!");
  }

  public StatusResponse getInvalidIdStatus(Long id) {
    return StatusResponse.error(String.format("Id: %d not found!", id));
  }

  public StatusResponse getInvalidBuildingLevelStatus() {
    return StatusResponse.error("Invalid building level!");
  }

  public StatusResponse getTroopIdNotReceivedStatus() {
    return StatusResponse.error("Troop ID not received");
  }

  public StatusResponse getTroopIdNotFoundStatus() {
    return StatusResponse.error("Troop ID not found");
  }

  public StatusResponse getUserNotFoundStatus (){ return StatusResponse.error("user_id not found"); }
}
