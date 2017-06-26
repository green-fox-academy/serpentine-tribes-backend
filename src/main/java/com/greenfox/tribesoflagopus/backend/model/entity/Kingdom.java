package com.greenfox.tribesoflagopus.backend.model.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Kingdom {

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Troop> troops = new ArrayList<>();

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Building> buildings = new ArrayList<>();

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Resource> resources = new ArrayList<>();

  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Setter
  private String name;

  @Setter
  @OneToOne
  private User user;

  @Setter
  @OneToOne(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private Location location;

  @Builder
  public Kingdom(String name) {
    this.name = name;
  }

  public void addTroop(Troop troop) {
    this.troops.add(troop);
    troop.setKingdom(this);
  }

  public void removeTroop(Troop troop) {
    troop.setKingdom(null);
    this.troops.remove(troop);
  }

  public void addBuilding(Building building) {
    this.buildings.add(building);
    building.setKingdom(this);
  }

  public void removeBuilding(Building building) {
    building.setKingdom(null);
    this.buildings.remove(building);
  }

  public void addResource(Resource resource) {
    resources.add(resource);
    resource.setKingdom(this);
  }

  public void removeResource(Resource resource) {
    resource.setKingdom(null);
    this.resources.remove(resource);
  }
}
