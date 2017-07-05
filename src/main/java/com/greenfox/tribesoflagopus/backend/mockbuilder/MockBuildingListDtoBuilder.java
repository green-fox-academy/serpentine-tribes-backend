package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.service.DtoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingListDtoBuilder {

  private BuildingListDto mockBuildingListDto;

  public MockBuildingListDtoBuilder(DtoService dtoService) {
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
