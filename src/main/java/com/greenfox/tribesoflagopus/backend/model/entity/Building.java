package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String buildingType;
  private int level;
  private int hp;

  public Building() {
  }

  public Building(String buildingType, int level, int hp) {
    this.buildingType = buildingType;
    this.level = 1;
    this.hp = hp;
  }

}