package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class UserTokenDto {

  @NotNull
  private String status;
  @NotNull
  private String token;
}
