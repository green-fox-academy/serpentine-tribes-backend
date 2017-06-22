package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by K on 2017.06.22..
 */

@Builder
@Getter
@Setter
public class PlayerDto implements JsonDto {

  private long id;
  private String username;
  private long kingdomId;

}
