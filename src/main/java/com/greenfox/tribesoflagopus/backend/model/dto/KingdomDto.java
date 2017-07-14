package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder(toBuilder = true)
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "user_id", "buildings", "resources", "troops", "location"})
public class KingdomDto implements JsonDto {

  @Singular
  private final List<TroopDto> troops;

  @Singular
  private final List<BuildingDto> buildings;

  @Singular
  private final List<ResourceDto> resources;

  private Long id;

  private String name;

  private Long userId;

  private LocationDto location;

  public void addBuildingDto(BuildingDto buildingDto) {
    this.buildings.add(buildingDto);
  }

  public void addBuildingDtoList(List<BuildingDto> listOfBuildingDtos) {
    this.buildings.addAll(listOfBuildingDtos);
  }

  public void addResourceDto(ResourceDto resourceDto) {
    this.resources.add(resourceDto);
  }

  public void addResourceDtoList(List<ResourceDto> listOfResourceDtos) {
    this.resources.addAll(listOfResourceDtos);
  }

  public void addTroopDto(TroopDto troopDto) {
    this.troops.add(troopDto);
  }

  public void addTroopDtoList(List<TroopDto> listOfTroopDtos) {
    this.troops.addAll(listOfTroopDtos);
  }
}

