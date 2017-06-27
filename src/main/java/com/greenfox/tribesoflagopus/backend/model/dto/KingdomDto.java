package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.Troop;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "user_id", "buildings", "resources", "troops", "location"})
public class KingdomDto implements JsonDto {

  @Singular
  private final List<Troop> troops;

  @Singular
  private final List<Building> buildings;

  @Singular
  private final List<Resource> resources;

  private long id;

  private String name;

  private Long userId;

  private Location location;

  public void addBuilding(Building building) {
    this.buildings.add(building);
  }

  public void addResource(Resource resource) {
    this.resources.add(resource);
  }

  public void addTroop(Troop troop) {
    this.troops.add(troop);
  }
}
