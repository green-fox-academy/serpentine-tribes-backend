package com.greenfox.tribesoflagopus.backend.mockbuilder;

import com.greenfox.tribesoflagopus.backend.model.dto.TroopDto;
import org.springframework.stereotype.Component;

@Component
public class MockUpdatedTroopDtoBuilder {

  private TroopDto mockTroopDto;

  public MockUpdatedTroopDtoBuilder() {
    this.mockTroopDto = TroopDto.builder()
        .id(2L)
        .level(2)
        .attack(2)
        .defence(2)
        .build();
  }

  public void setMockTroopDto(TroopDto mockTroopDto){
    this.mockTroopDto = mockTroopDto;
  }

  public TroopDto build() {
    return mockTroopDto;
  }
}
