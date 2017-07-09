package com.greenfox.tribesoflagopus.backend.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder
@Getter
@Setter
public class BuildingListDto implements JsonDto{

  @Singular
  private List<BuildingDto> buildings;

}
