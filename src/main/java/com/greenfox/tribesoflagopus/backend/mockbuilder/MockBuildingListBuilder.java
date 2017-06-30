package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockBuildingListBuilder {

  private List<Building> mockBuildingList;

  public MockBuildingListBuilder() {
    Building mockBuilding1 = Building.builder()
        .type("townhall")
        .build();
    Building mockBuilding2 = Building.builder()
        .type("farm")
        .build();
    mockBuilding1.setId(1L);
    mockBuilding2.setId(2L);
    this.mockBuildingList = new ArrayList<>();
    mockBuildingList.add(mockBuilding1);
    mockBuildingList.add(mockBuilding2);
  }

  public void setMockBuildingList(
      List<Building> mockBuildingList) {
    this.mockBuildingList = mockBuildingList;
  }

  public List<Building> build() {
    return mockBuildingList;
  }

}
