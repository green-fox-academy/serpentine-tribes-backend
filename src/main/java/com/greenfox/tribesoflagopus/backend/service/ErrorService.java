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
    List<FieldError> missingFields = bindingResult.getFieldErrors();
    ArrayList<String> missingFieldNames = new ArrayList<>();
    for (FieldError fieldError : missingFields) {
      missingFieldNames.add(fieldError.getField());
    }
    Collections.sort(missingFieldNames);

    String statusMessage = String.join(", ", missingFieldNames);

    StatusResponse missingParameterStatus = StatusResponse.builder()
        .status("error")
        .message("Missing parameter(s): " + statusMessage + "!")
        .build();
    return missingParameterStatus;
  }

  public StatusResponse getOccupiedUserNameStatus() {
    StatusResponse occupiedUserNameStatus = StatusResponse.builder()
        .status("error")
        .message("Username already taken, please choose an other one.")
        .build();
    return occupiedUserNameStatus;
  }

}
