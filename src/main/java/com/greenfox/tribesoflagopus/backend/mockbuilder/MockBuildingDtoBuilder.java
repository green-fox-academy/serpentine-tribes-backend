package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingDtoBuilder {

  private BuildingDto mockBuildingDto;

  public MockBuildingDtoBuilder(DtoService dtoService) {
    BuildingDto mockBuilding = BuildingDto.builder()
        .id(2L)
        .type("farm")
        .level(1)
        .hp(0)
        .build();
  }

  public void setMockBuildingDto(BuildingDto mockBuildingDto) {
    this.mockBuildingDto = mockBuildingDto;
  }

  public BuildingDto build() {
    return mockBuildingDto;
  }
}
