package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse implements JsonDto {

  private String status;
  private String message;

}
