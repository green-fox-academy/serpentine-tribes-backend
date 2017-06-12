package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse implements JsonDto {

  private long id;
  private String username;
  private long kingdomId;

}
