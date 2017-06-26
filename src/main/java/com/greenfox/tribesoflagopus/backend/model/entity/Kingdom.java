package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Setter
@NoArgsConstructor
public class Kingdom {

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Troop> troops = new ArrayList<>();

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Building> buildings = new ArrayList<>();

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Resource> resources = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String name;

  @OneToOne
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty(value = "user_id")
  private User user;

  @OneToOne(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private Location location;

  /**
   * {@code setLocation} is a <em>bidirectional</em> setter.
   * @param location The {@code Location} object to be set as the this {@code Kingdom}'s child entity
   */
  public void setLocation(Location location) {
    this.location = location;
    location.setKingdom(this);
  }

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
