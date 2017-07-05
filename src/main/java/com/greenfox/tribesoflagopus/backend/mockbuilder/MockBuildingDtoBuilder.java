package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingDtoBuilder {

  private final DtoService dtoService;
  private BuildingDto mockBuildingDto;

  @Autowired
  public MockBuildingDtoBuilder(DtoService dtoService) {
    this.dtoService = dtoService;
    Building mockBuilding = getMockBuilding();
    this.mockBuildingDto = dtoService.convertfromBuilding(mockBuilding);
  }

  private Building getMockBuilding() {
    Building mockBuilding = Building.builder()
        .type("farm")
        .build();
    mockBuilding.setId(2L);
    return mockBuilding;
  }

  public void setMockBuildingDto(BuildingDto mockBuildingDto) {
    this.mockBuildingDto = mockBuildingDto;
  }

  public BuildingDto build() {
    return mockBuildingDto;
  }
}
