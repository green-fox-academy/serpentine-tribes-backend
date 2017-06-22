package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse implements JsonDto {

  private String status;
  private String message;

}
