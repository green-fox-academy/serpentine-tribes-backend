package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "troop_sequence")
@EqualsAndHashCode
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
  private Timestamp startedAt;

  @Transient
  private Timestamp finishedAt;

  @Builder
  public Troop(int level, int hp, int attack, int defence, Timestamp startedAt) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
  }

  public static class TroopBuilder {
    private int level = 1;
  }
}
