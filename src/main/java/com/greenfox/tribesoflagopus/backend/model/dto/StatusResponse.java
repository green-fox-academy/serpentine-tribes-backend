package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse implements JsonDto {

  private String status;
  private String message;

  public static StatusResponse error(String message) {
    return new StatusResponse("error", message);
  }
}
