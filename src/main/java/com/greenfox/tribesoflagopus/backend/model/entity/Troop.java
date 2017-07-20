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

  @Builder
  public Troop(int level, int hp, int attack, int defence, Timestamp startedAt) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
  }

  public Timestamp getFinishedAt() {
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    long startedTimeAsMillis = startedAt.getTime();
    long minuteToDelayWith = 1;
    long delayInMillis = minuteToDelayWith * 60 * 1000;
    if (currentTime.getTime() - startedAt.getTime() < delayInMillis) {
      return new Timestamp(0);
    } else {
      return new Timestamp(startedTimeAsMillis + delayInMillis);
    }
  }

  public boolean isFinished () {
    return getFinishedAt().getTime() != 0;
  }

  public static class TroopBuilder {
    private int level = 1;
  }
}
