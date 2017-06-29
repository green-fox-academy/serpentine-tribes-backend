package com.greenfox.tribesoflagopus.backend.model.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BuildingLevelInputDto implements JsonDto {

  @NotNull
  private Integer level;

}
