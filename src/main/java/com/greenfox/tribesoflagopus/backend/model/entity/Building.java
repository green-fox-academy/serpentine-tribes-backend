package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String buildingType;
  private int level;
  private int hp;

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  @JoinColumn
  private Kingdom kingdom;

  public Building() {
  }

  public Building(String buildingType, int level, int hp) {
    this.buildingType = buildingType;
    this.level = 1;
    this.hp = hp;
  }

}