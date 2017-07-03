package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "building_sequence")
@Getter
@Setter
@NoArgsConstructor
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  private String type;
  private int level;
  private int hp;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Kingdom kingdom;

  @Builder
  public Building(String type) {
    this.type = type;
    this.level = 1;
    this.hp = 0;
  }

  public static class BuildingBuilder {
    private int level = 1;
  }
}