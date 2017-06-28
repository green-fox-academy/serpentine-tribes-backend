package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by K on 2017.06.22..
 */

@Builder
@Getter
@Setter
public class UserDto implements JsonDto {

  @NonNull
  private Long id;

  @NonNull
  private String username;
  private long kingdomId;
  private String token;

}
