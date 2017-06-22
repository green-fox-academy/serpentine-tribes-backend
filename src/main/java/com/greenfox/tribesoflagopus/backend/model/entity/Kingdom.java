package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Builder
  public Kingdom(String name, Player player,
          List<Troop> troops,
          List<Building> buildings,
          Location location,
          List<Resource> resources) {
    this.name = name;
    this.player = player;
    this.troops = troops;
    this.buildings = buildings;
    this.location = location;
    this.resources = resources;
  }

  private String name;

  @OneToOne
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private Player player;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Troop> troops;

  public void addTroop(Troop troop) {
    this.troops.add(troop);
    troop.setKingdom(this);
  }

  public void removeTroop(Troop troop) {
    troop.setKingdom(null);
    this.troops.remove(troop);
  }

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
  private List<Building> buildings;

  public void addBuilding(Building building) {
    this.buildings.add(building);
    building.setKingdom(this);
  }

  public void removeBuilding(Building building) {
    building.setKingdom(null);
    this.buildings.remove(building);
  }

  @OneToOne(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private Location location;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Resource> resources;

  public void addResources(Resource resource) {
    resources.add(resource);
    resource.setKingdom(this);
  }

  public void removeResources(Resource resource) {
    resource.setKingdom(null);
    this.resources.remove(resource);
  }
}
