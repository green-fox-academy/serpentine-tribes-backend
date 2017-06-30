package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MockKingdomBuilder {

  private Kingdom mockKingdom;

  public MockKingdomBuilder() {
    User mockUser = User.builder()
        .username("MockUser")
        .password("mockPassword")
        .avatar("mockAvatar")
        .points(1L)
        .build();
    mockUser.setId(1L);

    this.mockKingdom = Kingdom.builder()
        .name("MockKingdom")
        .build();
    mockKingdom.setId(1L);
    mockKingdom.setLocation(new Location(1, 1));

    mockUser.setKingdom(mockKingdom);
    mockKingdom.setUser(mockUser);

    Building mockBuilding1 = Building.builder()
        .type("townhall")
        .build();
    Building mockBuilding2 = Building.builder()
        .type("farm")
        .build();
    mockBuilding1.setId(1L);
    mockBuilding2.setId(2L);
    mockKingdom.addBuilding(mockBuilding1);
    mockKingdom.addBuilding(mockBuilding2);
  }

  public void setMockKingdom(Kingdom mockKingdom) {
    this.mockKingdom = mockKingdom;
  }

  public Kingdom build() {
    return mockKingdom;
  }
}
