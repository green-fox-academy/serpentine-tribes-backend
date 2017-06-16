package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull(message = "What is the type of the building?")
  private String buildingType;

  @NotNull(message = "What is the level of the building?")
  @Min(value = 1)
  private int level;

  @NotNull(message = "What is the hp of the building?")
  private int hp;

  public Building() {
  }

  public Building(String buildingType, int level, int hp) {
    this.buildingType = buildingType;
    this.level = 1;
    this.hp = hp;
  }

}