package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String name;
  private Long userId;

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
  private List<Building> buildings;

  public void addBuilding(Building building){
    this.buildings.add(building);
    building.setKingdom(this);
  }

  public void removeBuilding(Building building){
    building.setKingdom(null);
    this.buildings.remove(building);
  }
}
