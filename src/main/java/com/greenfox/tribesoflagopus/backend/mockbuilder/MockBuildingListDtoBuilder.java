package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingListDtoBuilder {

  private BuildingListDto mockBuildingListDto;

  public MockBuildingListDtoBuilder() {
    BuildingDto mockBuilding1 = BuildingDto.builder()
        .id(1L)
        .type("townhall")
        .level(1)
        .hp(0)
        .build();
    BuildingDto mockBuilding2 = BuildingDto.builder()
        .id(2L)
        .type("farm")
        .level(1)
        .hp(0)
        .build();
    this.mockBuildingListDto = BuildingListDto.builder()
         .building(mockBuilding1)
         .building(mockBuilding2)
         .build();
  }

  public void setMockBuildingListDto(BuildingListDto mockBuildingListDto) {
    this.mockBuildingListDto = mockBuildingListDto;
  }

  public BuildingListDto build() {
    return mockBuildingListDto;
  }

}
