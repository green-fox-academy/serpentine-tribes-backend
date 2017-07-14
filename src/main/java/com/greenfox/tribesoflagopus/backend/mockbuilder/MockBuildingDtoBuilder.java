package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingDto;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingDtoBuilder {

  private BuildingDto mockBuildingDto;

  public MockBuildingDtoBuilder() {
    this.mockBuildingDto = BuildingDto.builder()
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
