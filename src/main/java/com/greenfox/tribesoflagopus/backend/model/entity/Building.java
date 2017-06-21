package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
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

  @Builder
  public Building(String buildingType, int level, int hp) {
    this.buildingType = buildingType;
    this.level = 1;
    this.hp = hp;
  }

}