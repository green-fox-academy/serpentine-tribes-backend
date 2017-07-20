package com.greenfox.tribesoflagopus.backend.model.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "building_sequence")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  @Enumerated(EnumType.STRING)
  private BuildingType type;
  private int level;
  private int hp;
  private Timestamp startedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Kingdom kingdom;

  @Builder
  public Building(String type, Timestamp startedAt) {
    this.type = BuildingType.getByName(type);
    this.level = 1;
    this.hp = 0;
    this.startedAt = startedAt;
  }

  public Timestamp getFinishedAt() {
    if (type.equals(BuildingType.TOWNHALL) && level == 1) {
      return startedAt;
    }

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

  public static class BuildingBuilder {
    private int level = 1;
  }

  public int getFinishedLevel() {
    return isFinished() ? level : level-1;
  }
}