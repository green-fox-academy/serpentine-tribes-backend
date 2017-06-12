package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse implements JsonDto {

  private String status;
  private String message;

}
