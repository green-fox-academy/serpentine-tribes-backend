package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String buildingType;
  @Builder.Default private int level = 1;
  private int hp;

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  private Kingdom kingdom;

  @Builder
  public Building(String buildingType, int level, int hp) {
    this.buildingType = buildingType;
    this.level = level;
    this.hp = hp;
  }
}