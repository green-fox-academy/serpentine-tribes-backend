package com.greenfox.tribesoflagopus.backend.model.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "kingdom_sequence")
@EqualsAndHashCode
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  @Setter
  private String name;

  @Setter
  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  private User user;

  @OneToOne(mappedBy = "kingdom", cascade = CascadeType.ALL, orphanRemoval = true)
  private Location location;

  /**
   * The {@code setLocation} method is a <em>bidirectional</em> setter.
   * @param location The {@code Location} object to be set as this {@code Kingdom}'s child entity
   */
  public void setLocation(Location location) {
    this.location = location;
    location.setKingdom(this);
  }

  @Builder
  public Kingdom(String name) {
    this.name = name;
    this.addBuilding(createBuilding("townhall"));
    this.addResource(createResource("food"));
    this.addResource(createResource("gold"));
  }

  private Building createBuilding(String buildingType) {
    return Building.builder()
        .type(buildingType)
        .startedAt(new Timestamp(System.currentTimeMillis()))
        .build();
  }

  private Resource createResource(String resourceType) {
    return Resource.builder()
        .type(resourceType)
        .generation(10)
        .build();
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

  /**
   * The {@code addResource} method adds a {@code Resource} type object to the {@code Kingdom}.
   * The method's access level is private because {@code Resource} objects should only be created
   * when creating a brand new {@code Kingdom} through the static {@code builder()} method.
   *
   * @param resource The {@code Resource} object to be set as this {@code Kingdom}'s child entity
   */
  private void addResource(Resource resource) {
    resources.add(resource);
    resource.setKingdom(this);
  }

  /**
   * The {@code removeResource} method removes a {@code Resource} type object from the {@code Kingdom}.
   * The method's access level is private because at this time, {@code Resource} objects should
   * be permanently attached to their {@code Kingdom} <em>parent</em> entity.
   *
   * @param resource The {@code Resource} object to be removed from this {@code Kingdom}
   */
  private void removeResource(Resource resource) {
    resource.setKingdom(null);
    this.resources.remove(resource);
  }
}
