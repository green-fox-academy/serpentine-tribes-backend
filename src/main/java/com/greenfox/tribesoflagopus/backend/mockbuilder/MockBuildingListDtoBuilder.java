package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.BuildingListDto;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.service.DtoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingListDtoBuilder {

  private final DtoService dtoService;
  private BuildingListDto mockBuildingListDto;

  @Autowired
  public MockBuildingListDtoBuilder(DtoService dtoService) {
    List<Building> buildings = getMockBuildingList();
    mockBuildingListDto = dtoService.convertToBuildingListDtoFromBuildings(buildings);
    this.dtoService = dtoService;
  }

  private List<Building> getMockBuildingList() {
    List<Building> mockBuildingList= new ArrayList<>();
    Building mockBuilding1 = Building.builder()
        .type("townhall")
        .build();
    Building mockBuilding2 = Building.builder()
        .type("farm")
        .build();
    mockBuilding1.setId(1L);
    mockBuilding2.setId(2L);
    mockBuildingList.add(mockBuilding1);
    mockBuildingList.add(mockBuilding2);
    return mockBuildingList;
  }

  public void setMockBuildingListDto(BuildingListDto mockBuildingListDto) {
    this.mockBuildingListDto = mockBuildingListDto;
  }

  public BuildingListDto build() {
    return mockBuildingListDto;
  }

}
