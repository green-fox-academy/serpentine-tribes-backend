package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import java.sql.Timestamp;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingListDtoBuilder {

  private BuildingListDto mockBuildingListDto;

  public MockBuildingListDtoBuilder() {
    BuildingDto mockBuilding1 = BuildingDto.builder()
        .id(1L)
        .type(BuildingType.TOWNHALL)
        .level(1)
        .hp(0)
        .startedAt(new Timestamp(1500036357654L))
        .finishedAt(new Timestamp(0))
        .build();
    BuildingDto mockBuilding2 = BuildingDto.builder()
        .id(2L)
        .type(BuildingType.FARM)
        .level(1)
        .hp(0)
        .startedAt(new Timestamp(1500036357654L))
        .finishedAt(new Timestamp(0))
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
