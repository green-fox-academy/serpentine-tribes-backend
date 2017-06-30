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
@SequenceGenerator(name = "seq_store", sequenceName = "troop_sequence")
@Getter
@Setter
@NoArgsConstructor
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Kingdom kingdom;

  private int level = 1;
  private int hp;
  private int attack;
  private int defence;

  @Builder
  public Troop(int level, int hp, int attack, int defence) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
  }

  public static class TroopBuilder {
    private int level = 1;
  }
}
