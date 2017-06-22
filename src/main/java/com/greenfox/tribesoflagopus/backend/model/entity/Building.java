package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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